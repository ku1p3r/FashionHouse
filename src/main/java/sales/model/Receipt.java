package sales.model;

import common.model.Timestamp;

/**
 * @author Mason Hart
 */
public class Receipt implements Serializable {

    private static long ID = 0;

    long id;
    Sale sale;
    Timestamp timestamp;

    public Receipt(Sale s){
        this.sale = s;
        this.timestamp = s.getTimestamp();
        this.id = ID++;
    }

    public Price getTotal(){
        return sale.getTotal();
    }

    public String toString(){
        return "Receipt #" + id + "from " + timestamp + ":\n" + sale.toString();
    }

    @Override
    public String serialize() {
        return ""+id+"|"+sale.getId();
    }

    @Override
    public Object deserialize(String[] line) {
        return null;
    }

    public long getId() {
        return id;
    }

    public long getSaleId(){
        return sale.getId();
    }
}