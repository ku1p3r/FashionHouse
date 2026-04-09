package sales.service;

import common.model.Product;
import sales.model.Sale;
import sales.model.Receipt;
import common.util.Serializer;

import java.util.List;

/**
 * @author Mason Hart
 */
public class SalesService implements Service {

    private static final String SALES_TABLE_PATH = "res/sales.csv";
    private static final String PRODUCT_TABLE_PATH = "res/products.csv";

    private Serializer saleSerializer;
    private List<Product> availableProducts;
    private Sale currSale;

    public SalesService(){
        currSale = new Sale();
        saleSerializer = new Serializer(SALES_TABLE_PATH);
    }

    public void saveSale() throws Exception {
        currSale.serialize(saleSerializer);
    }

    public Sale getSale(int id){
        return null;
    }

    public void registerReturn(Receipt r){
        
    }

    @Override
    public void saveToDB() throws Exception {
        saveSale();
    }

    @Override
    public Object getFromDB(int id) {
        return getSale(id);
    }
}