package sales.service;

import common.model.Product;
import java.util.ArrayList;
import java.util.List;
import sales.model.Price;
import sales.model.Receipt;
import sales.model.Sale;

/**
 * @author Mason Hart
 */
public class SalesService /* implements Service */ {
    private final SalesDataRepository repository;
    private List<Product> availableProducts;
    private List<Product> cart;
    private List<String> pastSales;

    public SalesService(){
        this(new SalesAdapter());
    }

    public SalesService(SalesDataRepository repository){
        this.repository = repository;

        // empty cart
        this.cart = new ArrayList<>();

        // load available products
        this.availableProducts = new ArrayList<>(repository.loadAvailableProducts());

        loadPastSales();
    }

    public void loadPastSales(){
        this.pastSales = new ArrayList<>(repository.loadPastSales());
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
            repository.saveSale(s);
        } catch(Exception e){
            System.out.println("Fatal Error: Exception while saving sale");
            System.err.println(e);
        }

        // save the receipt
        Receipt r = new Receipt(s);
        try{
            repository.saveReceipt(r);
        } catch(Exception e){
            System.out.println("Fatal Error: Exception while saving sale");
            System.err.println(e);
        }

        loadPastSales();
    }
}