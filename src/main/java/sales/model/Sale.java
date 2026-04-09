package sales.model;

import common.model.Product;

public class Sale{

    List<Product> cart;
    Price subTotal;

    public Sale(){
        this.car = new ArrayList<>();
        this.subTotal = new Price(0,0);
    }

}