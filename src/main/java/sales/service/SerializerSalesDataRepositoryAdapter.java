package sales.service;

import common.model.Product;
import common.util.Serializer;
import java.util.ArrayList;
import java.util.List;
import sales.model.Receipt;
import sales.model.Sale;

/**
 * Serializer-backed adapter for sales persistence.
 */
public class SerializerSalesDataRepositoryAdapter implements SalesDataRepository {

    private static final String SALES_TABLE_PATH = "res/sales/sales.csv";
    private static final String PRODUCT_TABLE_PATH = "stores/fashionstore1.catalog";
    private static final String RECEIPT_TABLE_PATH = "res/sales/receipt.csv";

    private final Serializer saleSerializer;
    private final Serializer productSerializer;
    private final Serializer receiptSerializer;

    public SerializerSalesDataRepositoryAdapter() {
        this.saleSerializer = new Serializer(SALES_TABLE_PATH);
        this.productSerializer = new Serializer(PRODUCT_TABLE_PATH);
        this.receiptSerializer = new Serializer(RECEIPT_TABLE_PATH);
    }

    @Override
    public List<Product> loadAvailableProducts() {
        List<Product> products = new ArrayList<>();
        ArrayList<String> ids = productSerializer.get("id", String.class);
        for (int i = 0; i < ids.size(); i++) {
            String id = productSerializer.get("id", i, String.class);
            String name = productSerializer.get("name", i, String.class);
            String category = productSerializer.get("category", i, String.class);
            double price = productSerializer.get("price", i, Double.class);
            int quantity = productSerializer.get("quantity", i, Integer.class);
            String description = productSerializer.get("description", i, String.class);
            String supplier = productSerializer.get("supplier", i, String.class);
            String materials = "";
            try {
                materials = productSerializer.get("materials", i, String.class);
            } catch (Exception ignored) {
            }
            products.add(new Product(id, name, category, price, quantity, description, supplier, materials));
        }
        return products;
    }

    @Override
    public List<String> loadPastSales() {
        List<String> pastSales = new ArrayList<>();
        ArrayList<String> ids = saleSerializer.get("sale_id", String.class);
        for (int i = 0; i < ids.size(); i++) {
            String id = saleSerializer.get("sale_id", i, String.class);
            String timestamp = saleSerializer.get("timestamp", i, String.class);
            String total = saleSerializer.get("total", i, String.class);
            String saleString = String.format("%s|%s|%s", id, timestamp, total);
            pastSales.add(saleString);
        }
        return pastSales;
    }

    @Override
    public void saveSale(Sale sale) {
        saleSerializer.push(
            sale.getId(),
            sale.getTimestamp(),
            sale.getTotal()
        );
        saleSerializer.save();
    }

    @Override
    public void saveReceipt(Receipt receipt) {
        receiptSerializer.push(
            receipt.getId(),
            receipt.getSaleId()
        );
        receiptSerializer.save();
    }
}
