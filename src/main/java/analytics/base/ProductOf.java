package analytics.base;

import common.model.Product;
import common.model.Retailer;
import common.wrapper.Period;

public class ProductOf implements Metric {
    private final Metric a;
    private final Metric b;

    public ProductOf(Metric a, Metric b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public double evaluate(Retailer retailer, Iterable<Product> products, Period period){
        return a.evaluate(retailer, products, period) * b.evaluate(retailer, products, period);
    }
}
