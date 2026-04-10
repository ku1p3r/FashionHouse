package sales.model;

import common.model.Product;
import common.util.Serializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mason Hart
 */
public class Sale implements Serializable {

    private static long ID = 0;
    private static final double taxRate = 0.12;

    private long id;
    private List<Product> cart;
    private Price subTotal;
    private Timestamp timestamp;

    public Sale(List<Product> items){
        this.id = ID++;
        this.cart = new ArrayList<>();
        this.subTotal = new Price(0,0);
        this.timestamp = new Timestamp();
    }

    public Price getSubTotal() { return subTotal; }

    public Price getTotal(){
        return subTotal.add(getTaxes());
    }

    private Price getTaxes() {
        return new Price(0, 0); //TODO
    }

    public String toString(){
        String s = "Cart #" + id;
        s.concat("\n----------------------------------------------\n\n");
        for(Product p : cart){
            s.concat(p.toString());
        }
        s.concat("\n----------------------------------------------\n");
        return s;
    }

    @Override
    public String serialize(Serializer serializer) {
        return "";
    }

    @Override
    public Object deserialize(String[] line) {
        return null;
    }
}