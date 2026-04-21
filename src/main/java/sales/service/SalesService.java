package sales.service;

import common.model.Product;
import common.util.Serializer;
import java.util.ArrayList;
import java.util.List;
import sales.model.Price;
import sales.model.Receipt;
import sales.model.Sale;

/**
 * @author Mason Hart
 */
public class SalesService /* implements Service */ {

    private static final String SALES_TABLE_PATH = "res/sales/sales.csv";
    private static final String PRODUCT_TABLE_PATH = "stores/fashionstore1.catalog";
    private static final String RECEIPT_TABLE_PATH = "res/sales/receipt.csv";

    private Serializer saleSerializer;
    private Serializer productSerializer;
    private Serializer receiptSerializer;
    private List<Product> availableProducts;
    private List<Product> cart;
    private List<String> pastSales;

    public SalesService(){

        // initialize csv readers/writers
        this.saleSerializer = new Serializer(SALES_TABLE_PATH);
        this.productSerializer = new Serializer(PRODUCT_TABLE_PATH);
        this.receiptSerializer = new Serializer(RECEIPT_TABLE_PATH);

        // empty cart
        this.cart = new ArrayList<>();

        // load available products
        this.availableProducts = new ArrayList<>();
        ArrayList<String> ids = productSerializer.get("id", String.class);
        int rows = ids.size();
        for (int i = 0; i < rows; i++) {
           String id = productSerializer.get("id", i, String.class);
           String name = productSerializer.get("name", i, String.class);
           String category = productSerializer.get("category", i, String.class);
           double price = productSerializer.get("price", i, Double.class);
           int quantity = productSerializer.get("quantity", i, Integer.class);
           String description = productSerializer.get("description", i, String.class);
           String supplier = productSerializer.get("supplier", i, String.class);
           String materials = "";
           try { materials = productSerializer.get("materials", i, String.class); } catch (Exception ignored) {}
           availableProducts.add(new Product(id, name, category, price, quantity, description, supplier, materials));
        }

        loadPastSales();
    }

    public void loadPastSales(){
        this.pastSales = new ArrayList<>();
        ArrayList<String> ids = saleSerializer.get("sale_id", String.class);
        int rows = ids.size();
        for (int i = 0; i < rows; i++) {
            String id = saleSerializer.get("sale_id", i, String.class);
            String name = saleSerializer.get("timestamp", i, String.class);
            String category = saleSerializer.get("total", i, String.class);
            String saleString = String.format("%s|%s|%s", id, name, category);
            pastSales.add(saleString);
        }
    }

    public int numItemsInCart(){
        return cart.size();
    }

    public void printAvailableProducts(){
        for(Product p : availableProducts){
            System.out.println(p.toString());
        }
    }

    public void printCart() {
        for(Product p : cart){
            System.out.println(p.toString());
        }
    }

    public void printSales(){
        for(String s : pastSales){
            System.out.println(s);
        }
    }

    public void addToCart(String productID){
        for(Product p : availableProducts){
            if(p.getId().equals(productID)){
                cart.add(p);
                return;
            }
        }
    }

    public void removeFromCart(int i){
        cart.remove(i);
    }

    public void emptyCart(){
        cart = new ArrayList<>();
    }

    public String getCartProduct(int selected) {
        return cart.get(selected).getId();
    }

    public Price getTotal(){
        Sale s = new Sale(cart);
        return s.getTotal();
    }

    public void saveSale() {
        // save the sale
        Sale s = new Sale(cart);
        try{
            saleSerializer.push(
                    s.getId(),
                    s.getTimestamp(),
                    s.getTotal()
            );
            saleSerializer.save();
        } catch(Exception e){
            System.out.println("Fatal Error: Exception while saving sale");
            System.err.println(e);
        }

        // save the receipt
        Receipt r = new Receipt(s);
        try{
            receiptSerializer.push(
                    r.getId(),
                    r.getSaleId()
            );
            receiptSerializer.save();
        } catch(Exception e){
            System.out.println("Fatal Error: Exception while saving sale");
            System.err.println(e);
        }

        loadPastSales();
    }
}