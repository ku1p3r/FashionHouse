package sales.model;

import common.util.Serializer;
import sales.Serializable;

/**
 * @author Mason Hart
 */
public class Receipt implements Serializable {

    private static long ID = 0;

    long id;
    Sale sale;
    int timestamp;

    public Receipt(Sale s, int timestamp){
        this.sale = s;
        this.timestamp = timestamp;
        this.id = ID++;
    }

    public Price getTotal(){
        return sale.getTotal();
    }

    public String toString(){
        return "Receipt #" + id + "from " + timestamp + ":\n" + sale.toString();
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