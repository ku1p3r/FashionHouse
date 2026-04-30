package analytics.base;

import common.model.Product;
import common.model.Retailer;
import common.wrapper.Period;

public class SumOverRetailers implements Metric {
    private final Metric child;
    private final Iterable<Retailer> retailers;

    public SumOverRetailers(Metric child, Iterable<Retailer> retailers){
        this.child = child;
        this.retailers = retailers;
    }

    @Override
    public double evaluate(Retailer retailer, Iterable<Product> products, Period period){
        double total = 0.0;
        for(Retailer r : retailers){
            total += child.evaluate(r, products, period);
        }
        return total;
    }
}
