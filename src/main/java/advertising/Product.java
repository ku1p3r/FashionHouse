package advertising;
/*
 * 
 * Advertising Module - Iteration 1
 *
 *  Product promoted in an advertisement.
 * Author: Gilbert
 */
public class Product {
    private final int productId;
    private final String name;

    public Product(int productId, String name) {
        this.productId = productId;
        this.name = name;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }
}
