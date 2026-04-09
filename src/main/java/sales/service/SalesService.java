package sales.service;

import common.model.Product;
import sales.model.Sale;
import sales.model.Receipt;
import common.util.Serializer;

import java.util.List;

/**
 * @author Mason Hart
 */
public class SalesService{

    private static final String SALES_TABLE_PATH = "res/sales.csv";
    private static final String PRODUCT_TABLE_PATH = "res/products.csv";

    private Serializer dbms;
    private List<Product> availableProducts;
    private Sale currSale;

    public SalesService(){
        currSale = new Sale();
        dbms = new Serializer(SALES_TABLE_PATH);
    }

    public void saveSale() throws Exception {
        currSale.serialize(dbms);
    }

    public void getSale(int id){

    }

    public void registerReturn(Receipt r){
        
    }
}