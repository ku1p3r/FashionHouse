package analytics.base;

import common.model.Product;
import common.model.Retailer;
import common.wrapper.Period;

import java.util.HashMap;

public class SeasonalIndex implements Metric {
    private final Metric child;
    private final int targetMonth;
    private final int startYear;
    private final int endYear;

    public SeasonalIndex(Metric child, int targetMonth, int startYear, int endYear){
        this.child = child;
        this.targetMonth = targetMonth;
        this.startYear = startYear;
        this.endYear = endYear;
    }

    @Override
    public double evaluate(Retailer retailer, Iterable<Product> products, Period period){
        HashMap<Integer, Double> monthlySums = new HashMap<>();
        HashMap<Integer, Integer> monthCounts = new HashMap<>();

        for(int year = startYear; year <= endYear; year++){
            for(int month = 1; month <= 12; month++){
                double sales = child.evaluate(retailer, products, new Period(month, year));
                if(sales > 0){
                    monthlySums.merge(month, sales, Double::sum);
                    monthCounts.merge(month, 1, Integer::sum);
                }
            }
        }

        HashMap<Integer, Double> monthlyAverages = new HashMap<>();
        double overallSum = 0.0;
        for(int month = 1; month <= 12; month++){
            int count = monthCounts.getOrDefault(month, 0);
            double avg = count == 0 ? 0.0 : monthlySums.getOrDefault(month, 0.0) / count;
            monthlyAverages.put(month, avg);
            overallSum += avg;
        }

        double grandAverage = overallSum / 12.0;
        if(grandAverage == 0.0){
            return 1.0;
        }
        return monthlyAverages.get(targetMonth) / grandAverage;
    }
}
