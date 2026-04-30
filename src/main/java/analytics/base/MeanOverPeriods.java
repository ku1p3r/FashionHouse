package analytics.base;

import common.model.Product;
import common.model.Retailer;
import common.wrapper.Period;

public class MeanOverPeriods implements Metric {
    private final SumOverPeriods sum;
    private final int monthCount;

    public MeanOverPeriods(Metric child, Period start, Period end){
        this.sum = new SumOverPeriods(child, start, end);
        this.monthCount = countMonths(start, end);
    }

    @Override
    public double evaluate(Retailer retailer, Iterable<Product> products, Period ignored){
        if(monthCount == 0){
            return 0.0;
        }
        return sum.evaluate(retailer, products, null) / monthCount;
    }

    private static int countMonths(Period start, Period end) {
        int months = ((end.getYear() - start.getYear()) * 12) + (end.getMonth() - start.getMonth()) + 1;
        return Math.max(months, 0);
    }
}
