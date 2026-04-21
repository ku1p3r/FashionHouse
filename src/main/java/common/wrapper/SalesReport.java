package common.wrapper;

import common.model.Product;

import java.util.HashMap;

public record SalesReport(
        Period period,
        HashMap<Product, Integer> sales
) {
}
