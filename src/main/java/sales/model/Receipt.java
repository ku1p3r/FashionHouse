package sales.model;

/**
 * @author Mason Hart
 */
public class Receipt{

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

}