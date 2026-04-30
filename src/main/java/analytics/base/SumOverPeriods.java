package analytics.base;

import common.model.Product;
import common.model.Retailer;
import common.wrapper.Period;

public class SumOverPeriods implements Metric {
    private final Metric child;
    private final Period start;
    private final Period end;

    public SumOverPeriods(Metric child, Period start, Period end){
        this.child = child;
        this.start = start;
        this.end = end;
    }

    @Override
    public double evaluate(Retailer retailer, Iterable<Product> products, Period period){
        double total = 0.0;
        int month = start.getMonth();
        int year = start.getYear();
        while(year < end.getYear() || (year == end.getYear() && month <= end.getMonth())){
            total += child.evaluate(retailer, products, new Period(month, year));
            month++;
            if(month > 12){
                month = 1;
                year++;
            }
        }
        return total;
    }
}
