package analytics.services;

import analytics.base.Metric;
import common.model.Product;
import common.model.Retailer;
import common.wrapper.Period;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class TopProducts {
    private TopProducts(){}

    public static List<Product> rank(Retailer retailer, Metric scorer, Period period, int n){
        List<Product> products = new ArrayList<>(retailer.getProducts());
        products.sort(Comparator.comparingDouble(
                (Product p) -> scorer.evaluate(retailer, List.of(p), period)
        ).reversed());
        return products.subList(0, Math.min(n, products.size()));
    }
}
