package analytics.services;

import analytics.wrappers.TrendDirection;
import analytics.wrappers.TrendResult;
import common.model.Product;
import common.model.Retailer;
import common.util.EntityLoader;
import common.util.Terminal;
import common.wrapper.Option;
import common.wrapper.Period;
import java.util.*;

/**
 * Service for analyzing trends and data.
 *
 * Author: Jase Beaubien
 */
public class TrendService {
    public TrendService() {
        boolean[] running = {true};
        while(running[0]){
            Terminal.prompt("Report Usage", List.of(), List.of(
                    new Option("trend", "Observe trends.", () -> {
                        EntityLoader el = new EntityLoader();
                        TrendResult result = getTrend("test", el.getAllRetailers(), el.getAllProducts(), new Period(1, 2023), new Period(6, 2029));
                        System.out.println(result.direction() + " by " + result.percentageChange() + "%");
                    }),
                    new Option("mean", "Observe mean.", () -> {
                        EntityLoader el = new EntityLoader();
                        float result = getTotalMeanAnnualSales(el.getAllRetailers().iterator().next(), new Period(1, 2027));
                        System.out.println("Mean: " + result);
                    }),
                    new Option("estimate", "Anticipate.", () -> {
                        EntityLoader el = new EntityLoader();
                        HashMap<Product, Integer> result = anticipateStock(el.getAllRetailers().iterator().next(), new Period(1, 2027));
                        for(Product product : result.keySet()){
                            System.out.println(product.getName() + ": " + result.get(product));
                        }
                    }),
                    new Option("volatile", "Observe volatility.", () -> {
                        EntityLoader el = new EntityLoader();
                        float result = getSalesVolatility(el.getAllRetailers().iterator().next(), el.getAllProducts(), new Period(1, 2023), new Period(6, 2029));
                        System.out.println("Volatility: " + result);
                    }),
                    new Option("top", "Observe top.", () -> {
                        EntityLoader el = new EntityLoader();
                        int i = 1;
                        for(Product product : getTopProducts(el.getAllRetailers().iterator().next(), new Period(1, 2027), 3)){
                            System.out.println("(" + (i++) + ") " + product.getName());
                        }
                    }),
                    new Option("season", "Observe seasonal indexes.", () -> {
                        EntityLoader el = new EntityLoader();
                        HashMap<Integer, Float> result = getSeasonalIndexes(el.getAllRetailers().iterator().next(), el.getAllProducts(), 2023, 2029);
                        for (int month = 1; month <= 12; month++) {
                            System.out.printf("Month %2d: %.4f%n", month, result.get(month));
                        }
                    }),
                    new Option("back", "Quit to previous menu.", () -> running[0] = false)
            ));
        }
    }

    public static TrendResult getTrend(
            String metricName,
            Iterable<Retailer> retailers,
            Iterable<Product> products,
            Period start,
            Period end
    ) {
        List<Period> periods = new ArrayList<>();
        int month = start.getMonth();
        int year = start.getYear();
        while(year < end.getYear() || (year == end.getYear() && month <= end.getMonth())){
            periods.add(new Period(month, year));
            month++;
            if(month > 12){
                month = 1;
                year++;
            }
        }

        if(periods.isEmpty()){
            return new TrendResult(metricName, TrendDirection.STABLE, 0.0);
        }

        List<Integer> periodTotals = new ArrayList<>();
        for(Period period : periods){
            int total = 0;
            for(Retailer retailer : retailers){
                total += retailer.getSales(products, period);
            }
            periodTotals.add(total);
        }

        List<Integer> nonZeroPeriods = new ArrayList<>();
        for(Integer total : periodTotals){
            if (total > 0) {
                nonZeroPeriods.add(total);
            }
        }

        if(nonZeroPeriods.isEmpty()){
            return new TrendResult(metricName, TrendDirection.STABLE, 0.0);
        }

        double firstPeriod = nonZeroPeriods.get(0);
        double lastPeriod = nonZeroPeriods.get(nonZeroPeriods.size() - 1);
        double percentageChange = ((lastPeriod - firstPeriod) / firstPeriod) * 100;
        percentageChange = Math.round(percentageChange * 100.0) / 100.0;

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
        HashMap<Product, Integer> result = getMeanAnnualSales(retailer, period);
        float total = 0f;
        for(Product product : result.keySet()){
            total += result.get(product);
        }
        return total;
    }

    public static Set<Product> getTopProducts(Retailer retailer, Period period, int n) {
        HashMap<Product, Integer> sales = new HashMap<>();
        for (Product product : retailer.getProducts()) {
            sales.put(product, retailer.getSales(List.of(product), period));
        }

        List<Map.Entry<Product, Integer>> entries = new ArrayList<>(sales.entrySet());
        entries.sort((a, b) -> b.getValue() - a.getValue());

        Set<Product> result = new LinkedHashSet<>();
        for (int i = 0; i < Math.min(n, entries.size()); i++) {
            result.add(entries.get(i).getKey());
        }
        return result;
    }

    public static HashMap<Product, Integer> getMeanAnnualSales(Retailer retailer, Period period) {
        HashMap<Product, Integer> result = new HashMap<>();
        int month = period.getMonth();
        int year = period.getYear() - 1;
        for(int i = 0; i < 12; i++){
            month += 1;
            if(month > 12){
                month = 1;
                year += 1;
            }
            Period current = new Period(month, year);

            for(Product product : retailer.getProducts()){
                int num = retailer.getSales(List.of(product), current);

                if(result.containsKey(product)){
                    result.replace(product, result.get(product) + num);
                }else{
                    result.put(product, num);
                }
            }
        }

        for(Product product : result.keySet()){
            result.replace(product, (int)Math.ceil(result.get(product) / 12.0f));
        }

        return result;
    }

    public static float getSalesVolatility(
            Retailer retailer,
            Iterable<Product> products,
            Period start,
            Period end
    ){
        List<Integer> sales = new ArrayList<>();
        int month = start.getMonth();
        int year = start.getYear();
        while(year < end.getYear() || (year == end.getYear() && month <= end.getMonth())){
            Period current = new Period(month, year);
            sales.add(retailer.getSales(products, current));
            month++;
            if(month > 12){
                month = 1;
                year++;
            }
        }
        int size = sales.size();
        if(size == 0){
            return 0f;
        }

        double mean = 0.0;
        for(Integer sale : sales){
            mean += (double)sale;
        }
        mean /= size;
        if(mean == 0.0){
            return 0f;
        }

        double sum = 0.0;
        for(Integer sale : sales){
            sum += Math.pow(sale - mean, 2);
        }
        double dev = Math.sqrt(sum / size);

        return (float)(dev / mean);
    }

    public static HashMap<Integer, Float> getSeasonalIndexes(
            Retailer retailer,
            Iterable<Product> products,
            int startYear,
            int endYear
    ) {
        HashMap<Integer, Integer> monthlySums = new HashMap<>();
        HashMap<Integer, Integer> monthCounts = new HashMap<>();

        for(int year = startYear; year <= endYear; year++){
            for(int month = 1; month <= 12; month++){
                int sales = retailer.getSales(products, new Period(month, year));
                if(sales > 0){
                    monthlySums.merge(month, sales, Integer::sum);
                    monthCounts.merge(month, 1, Integer::sum);
                }
            }
        }

        HashMap<Integer, Float> monthlyAverages = new HashMap<>();
        float overallSum = 0.0f;
        for(int month = 1; month <= 12; month++){
            int count = monthCounts.getOrDefault(month, 0);
            float avg = count == 0 ? 0.0f : monthlySums.getOrDefault(month, 0) / (float) count;
            monthlyAverages.put(month, avg);
            overallSum += avg;
        }

        float grandAverage = overallSum / 12.0f;
        HashMap<Integer, Float> indexes = new HashMap<>();
        for(int month = 1; month <= 12; month++){
            indexes.put(month, grandAverage == 0.0f ? 1.0f : monthlyAverages.get(month) / grandAverage);
        }
        return indexes;
    }

    public static HashMap<Product, Integer> anticipateStock(Retailer retailer, Period period) {
        HashMap<Product, Integer> result = new HashMap<>();

        HashMap<Product, Integer> MAS = getMeanAnnualSales(
                retailer,
                new Period(period.getMonth() - 1, period.getYear())
        );
        HashMap<Product, Integer> lastMAS = getMeanAnnualSales(
                retailer,
                new Period(period.getMonth() - 1, period.getYear() - 1)
        );

        int endYear = period.getYear() - 1;
        HashMap<Integer, Float> seasonalIndexes = getSeasonalIndexes(
                retailer,
                retailer.getProducts(),
                endYear - 2,
                endYear
        );
        float seasonalIndex = seasonalIndexes.getOrDefault(period.getMonth(), 1.0f);

        for(Product product : retailer.getProducts()){
            int mas = MAS.getOrDefault(product, 0);
            System.out.println("MAS: " + mas);
            System.out.println("Seasonal Index: " + seasonalIndex);
            int anticipated = (int)Math.ceil(seasonalIndex * mas);
            result.put(product, anticipated);
        }

        return result;
    }
}
