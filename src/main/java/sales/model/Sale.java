package sales.model;

import common.model.Product;
import common.util.Serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Mason Hart
 */
public class Sale implements Serializable {

    private static long ID = new Random().nextLong(0, 2_000_000_000); // this is a hack :/
    private static final double taxRate = 0.12;

    private long id;
    private List<Product> cart;
    private Price subTotal;
    private Timestamp timestamp;

    public Sale(List<Product> items){
        this.id = ID++;
        this.cart = new ArrayList<>();
        this.timestamp = new Timestamp();

        this.subTotal = new Price(0,0);
        for(Product p : items){
            subTotal = subTotal.add(new Price(p.getPrice()));
        }
    }

    public Price getSubTotal() { return subTotal; }

    public Price getTotal(){
        return subTotal.add(getTaxes());
    }

    private Price getTaxes() {
        return new Price(0, 0); //TODO
    }

    public long getId(){ return this.id; }

    public Timestamp getTimestamp(){
        return this.timestamp;
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
    public String serialize() {
        return ""+id+"|"+timestamp.toString()+"|"+getTotal().toString();
    }

    @Override
    public Object deserialize(String[] line) {
        return null;
    }
}