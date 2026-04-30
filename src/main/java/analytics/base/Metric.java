package analytics.base;

import common.model.Product;
import common.model.Retailer;
import common.wrapper.Period;

public interface Metric {
    double evaluate(
            Retailer retailer,
            Iterable<Product> products,
            Period period
    );
}
