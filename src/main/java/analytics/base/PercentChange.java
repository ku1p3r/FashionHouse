package analytics.base;

import analytics.wrappers.TrendDirection;
import analytics.wrappers.TrendResult;
import common.model.Product;
import common.model.Retailer;
import common.wrapper.Period;

import java.util.ArrayList;
import java.util.List;

public class PercentChange implements Metric {
    private final Metric child;
    private final Period start;
    private final Period end;

    public PercentChange(Metric child, Period start, Period end){
        this.child = child;
        this.start = start;
        this.end = end;
    }

    @Override
    public double evaluate(Retailer retailer, Iterable<Product> products, Period period){
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

        List<Double> periodTotals = new ArrayList<>();
        for(Period p : periods){
            periodTotals.add(child.evaluate(retailer, products, p));
        }

        List<Double> nonZeroPeriods = new ArrayList<>();
        for(Double total : periodTotals){
            if(total > 0){
                nonZeroPeriods.add(total);
            }
        }

        if(nonZeroPeriods.isEmpty()){
            return 0.0;
        }

        double firstPeriod = nonZeroPeriods.get(0);
        double lastPeriod = nonZeroPeriods.get(nonZeroPeriods.size() - 1);
        return ((lastPeriod - firstPeriod) / firstPeriod) * 100;
    }
}
