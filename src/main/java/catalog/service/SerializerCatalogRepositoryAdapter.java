package catalog.service;

import common.model.Product;
import common.util.Serializer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Serializer-backed adapter for catalog persistence.
 */
public class SerializerCatalogRepositoryAdapter implements CatalogRepository {

    private static final String[] HEADER = new String[]{
        "id", "name", "category", "price", "quantity", "description", "supplier", "materials"
    };

    private final Path filePath;

    public SerializerCatalogRepositoryAdapter(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Product> loadAll() throws IOException {
        List<Product> catalog = new ArrayList<>();
        if (!Files.exists(filePath)) {
            Serializer s = new Serializer(HEADER);
            try {
                s.save(filePath.toString());
            } catch (Exception e) {
                throw new IOException(e);
            }
            return catalog;
        }

        Serializer s = new Serializer(filePath.toString());
        ArrayList<String> ids;
        try {
            ids = s.get("id", String.class);
        } catch (Exception e) {
            throw new IOException("Failed to load catalog: " + e.getMessage());
        }
        for (int i = 0; i < ids.size(); i++) {
            try {
                String id = s.get("id", i, String.class);
                String name = s.get("name", i, String.class);
                String category = s.get("category", i, String.class);
                double price = s.get("price", i, Double.class);
                int quantity = s.get("quantity", i, Integer.class);
                String description = s.get("description", i, String.class);
                String supplier = s.get("supplier", i, String.class);
                String materials = "";
                try {
                    materials = s.get("materials", i, String.class);
                } catch (Exception ignored) {
                }
                catalog.add(new Product(id, name, category, price, quantity, description, supplier, materials));
            } catch (Exception e) {
                throw new IOException("Parse error at row " + (i + 1) + ": " + e.getMessage());
            }
        }
        return catalog;
    }

    @Override
    public void saveAll(List<Product> products) throws IOException {
        Serializer s = new Serializer(HEADER);
        try {
            for (Product p : products) {
                s.push(
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    String.valueOf(p.getPrice()),
                    String.valueOf(p.getQuantity()),
                    p.getDescription(),
                    p.getSupplier(),
                    p.getMaterials()
                );
            }
            s.save(filePath.toString());
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
