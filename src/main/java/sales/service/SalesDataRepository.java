package sales.service;

import common.model.Product;
import java.util.List;
import sales.model.Receipt;
import sales.model.Sale;

/**
 * @author Mason Hart
 */
public interface SalesDataRepository {

    List<Product> loadAvailableProducts();

    List<String> loadPastSales();

    void saveSale(Sale sale);

    void saveReceipt(Receipt receipt);
}
