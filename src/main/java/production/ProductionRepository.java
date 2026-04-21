package production;

import catalog.service.CatalogService;
import common.util.Serializer;
import common.util.Terminal;
import java.util.ArrayList;
import java.util.List;

public class ProductionRepository {

    private final CatalogService service;
    private List<ProductionBatch> batches = new ArrayList<>();

    // Product catalog data (parallel lists loaded from products.csv)
    private List<String> productIds   = new ArrayList<>();
    private List<String> productNames = new ArrayList<>();
    private List<String> productCategories = new ArrayList<>();
    private List<Double> productPrices = new ArrayList<>();
    private List<Integer> productQtys  = new ArrayList<>();
    private List<String> productDescs  = new ArrayList<>();
    private List<String> productSuppliers = new ArrayList<>();
    private List<String> productMaterials = new ArrayList<>();

    public ProductionRepository(CatalogService service) {
        this.service = service;
        loadBatches();
        loadProducts();
    }

    // Loading

    private void loadBatches() {
        try {
            Serializer s = new Serializer(DataPaths.BATCHES);
            for (int i = 0; i < s.size(); i++) {
                batches.add(new ProductionBatch(
                        s.get("id",            i, String.class),
                        s.get("productId",     i, String.class),
                        s.get("productName",   i, String.class),
                        s.get("requestedBy",   i, String.class),
                        s.get("batchSize",     i, Integer.class),
                        ProductionBatch.Status.valueOf(s.get("status", i, String.class)),
                        s.get("materialUsage", i, String.class),
                        s.get("createdDate",   i, String.class)
                ));
            }
        } catch (Exception e) {
            Terminal.printError("Could not load production batches: " + e.getMessage());
        }
    }

    private void loadProducts() {
        try {
            Serializer s = new Serializer(service.getFilePath().toString());
            for (int i = 0; i < s.size(); i++) {
                productIds.add(s.get("id",          i, String.class));
                productNames.add(s.get("name",       i, String.class));
                productCategories.add(s.get("category", i, String.class));
                productPrices.add(s.get("price",     i, Double.class));
                productQtys.add(s.get("quantity",    i, Integer.class));
                productDescs.add(s.get("description",i, String.class));
                productSuppliers.add(s.get("supplier",i, String.class));
                try { productMaterials.add(s.get("materials", i, String.class)); } catch (Exception ex) { productMaterials.add(""); }
            }
        } catch (Exception e) {
            Terminal.printError("Could not load products: " + e.getMessage());
        }
    }

    // Saving

    public void saveBatches() {
        try {
            Serializer s = new Serializer(new String[]{
                "id","productId","productName","requestedBy",
                "batchSize","status","materialUsage","createdDate"
            });
            for (ProductionBatch b : batches) {
                s.push(b.getId(), b.getProductId(), b.getProductName(),
                        b.getRequestedBy(), b.getBatchSize(), b.getStatus().name(),
                        b.getMaterialUsage(), b.getCreatedDate());
            }
            s.save(DataPaths.BATCHES);
        } catch (Exception e) {
            Terminal.printError("Failed to save batches: " + e.getMessage());
        }
    }

    /** Update product quantity in products.csv after production completes. */
    public void updateProductQuantity(String productId, int addedQty) {
        int idx = productIds.indexOf(productId);
        if (idx < 0) {
            Terminal.printError("Product ID not found in catalog: " + productId);
            return;
        }
        productQtys.set(idx, productQtys.get(idx) + addedQty);
        saveProducts();
    }

    private void saveProducts() {
        try {
            Serializer s = new Serializer(new String[]{
                "id","name","category","price","quantity","description","supplier","materials"
            });
            for (int i = 0; i < productIds.size(); i++) {
                s.push(productIds.get(i), productNames.get(i), productCategories.get(i),
                        productPrices.get(i), productQtys.get(i),
                        productDescs.get(i), productSuppliers.get(i), productMaterials.get(i));
            }
            s.save(service.getFilePath().toString());
        } catch (Exception e) {
            Terminal.printError("Failed to save products: " + e.getMessage());
        }
    }

    // Queries

    public List<ProductionBatch> getAll()           { return batches; }

    public List<ProductionBatch> getActive() {
        List<ProductionBatch> active = new ArrayList<>();
        for (ProductionBatch b : batches) {
            if (b.getStatus() == ProductionBatch.Status.PENDING || b.getStatus() == ProductionBatch.Status.IN_PROGRESS)
                active.add(b);
        }
        return active;
    }

    public ProductionBatch findById(String id) {
        for (ProductionBatch b : batches) if (b.getId().equalsIgnoreCase(id)) return b;
        return null;
    }

    /** Returns list of [id, name] pairs for all products. */
    public List<String[]> getProductList() {
        List<String[]> list = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i++)
            list.add(new String[]{ productIds.get(i), productNames.get(i), String.valueOf(productQtys.get(i)) });
        return list;
    }

    public String getProductMaterials(String productId) {
        int idx = productIds.indexOf(productId);
        return idx >= 0 ? productMaterials.get(idx) : "";
    }

    public String generateBatchId() {
        int max = 0;
        for (ProductionBatch b : batches) {
            try {
                int n = Integer.parseInt(b.getId().replace("B-", ""));
                if (n > max) max = n;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("B-%03d", max + 1);
    }

    public void addBatch(ProductionBatch b) { batches.add(b); }

    public int getProductQty(String productId) {
        int idx = productIds.indexOf(productId);
        return idx >= 0 ? productQtys.get(idx) : -1;
    }
}
