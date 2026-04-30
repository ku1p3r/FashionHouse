package analytics.services;

import analytics.base.*;
import analytics.wrappers.TrendDirection;
import analytics.wrappers.TrendResult;
import common.model.Product;
import common.model.Retailer;
import common.wrapper.Period;
import java.util.*;

/**
 * Service for analyzing trends and data.
 *
 * Author: Jase Beaubien
 */
public class TrendService {
    public static TrendResult getTrend(
            String metricName,
            Iterable<Retailer> retailers,
            Iterable<Product> products,
            Period start,
            Period end
    ) {
        double percentageChange = new PercentChange(
                new SumOverRetailers(new RawSales(), retailers),
                start,
                end
        ).evaluate(null, products, null);

        TrendDirection direction;
        if(percentageChange > 0){
            direction = TrendDirection.RISING;
        }else if(percentageChange < 0){
            direction = TrendDirection.FALLING;
        }else{
            direction = TrendDirection.STABLE;
        }

        return new TrendResult(metricName, direction, percentageChange);
    }

    public static float getTotalMeanAnnualSales(Retailer retailer, Period period) {
        Metric mean = new MeanOverPeriods(
                new RawSales(),
                period.minusMonths(11),
                period
        );

        return (float)mean.evaluate(retailer, retailer.getProducts(), null);
    }

    public static Set<Product> getTopProducts(Retailer retailer, Period period, int n) {
        return new LinkedHashSet<>(
                TopProducts.rank(retailer, new RawSales(), period, n)
        );
    }

    public static HashMap<Product, Integer> getMeanAnnualSales(Retailer retailer, Period period) {
        Metric mean = new MeanOverPeriods(
                new RawSales(),
                period.minusMonths(11),
                period
        );

        HashMap<Product, Integer> result = new HashMap<>();
        for(Product product : retailer.getProducts()){
            double avg = mean.evaluate(retailer, List.of(product), null);
            result.put(product, (int)Math.ceil(avg));
        }

        return result;
    }

    public static double getSalesVolatility(
            Retailer retailer,
            Iterable<Product> products,
            Period start,
            Period end
    ){
        Metric volatility = new CoefficientOfVariation(new RawSales(), start, end);
        return volatility.evaluate(retailer, products, null);
    }

    public static HashMap<Integer, Float> getSeasonalIndexes(
            Retailer retailer,
            Iterable<Product> products,
            int startYear,
            int endYear
    ) {
        HashMap<Integer, Float> indexes = new HashMap<>();
        for(int month = 1; month <= 12; month++){
            Metric index = new SeasonalIndex(new RawSales(), month, startYear, endYear);
            indexes.put(month, (float)index.evaluate(retailer, products, null));
        }
        return indexes;
    }

    public static HashMap<Product, Integer> anticipateStock(Retailer retailer, Period period) {
        int endYear = period.getYear() - 1;

        Metric anticipated = new ProductOf(
                new MeanOverPeriods(new RawSales(), period.minusMonths(12), period.minusMonths(1)),
                new SeasonalIndex(new RawSales(), period.getMonth(), endYear - 2, endYear)
        );

        HashMap<Product, Integer> result = new HashMap<>();
        for(Product product : retailer.getProducts()){
            double v = anticipated.evaluate(retailer, List.of(product), null);
            result.put(product, (int)Math.ceil(v));
        }

        return result;
    }
}
