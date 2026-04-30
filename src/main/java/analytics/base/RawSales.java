package analytics.base;

import common.model.Product;
import common.model.Retailer;
import common.wrapper.Period;

public class RawSales implements Metric {
    @Override
    public double evaluate(Retailer retailer, Iterable<Product> products, Period period){
        return retailer.getSales(products, period);
    }
}
