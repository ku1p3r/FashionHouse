package catalog.service;

import catalog.model.ValidationResult;
import common.model.Product;
import common.util.Serializer;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class CatalogService {

    private final Path filePath;
    private final List<Product> catalog = new ArrayList<>();

    public CatalogService(Path filePath) throws IOException {
        this.filePath = filePath;
        load();
    }

    private void load() throws IOException {
        catalog.clear();
        if (!Files.exists(filePath)) {
            Serializer s = new Serializer(new String[]{"id","name","category","price","quantity","description","supplier"});
            try { s.save(filePath.toString()); }
            catch (Exception e) { throw new IOException(e); }
            return;
        }

        Serializer s = new Serializer(filePath.toString());
        ArrayList<String> ids;
        try {
            ids = s.get("id", String.class);
        } catch (Exception e) {
            throw new IOException("Failed to load catalog: " + e.getMessage());
        }
        int rows = ids.size();
        for (int i = 0; i < rows; i++) {
            try {
                String id = s.get("id", i, String.class);
                String name = s.get("name", i, String.class);
                String category = s.get("category", i, String.class);
                double price = s.get("price", i, Double.class);
                int quantity = s.get("quantity", i, Integer.class);
                String description = s.get("description", i, String.class);
                String supplier = s.get("supplier", i, String.class);
                catalog.add(new Product(id, name, category, price, quantity, description, supplier));
            } catch (Exception e) {
                throw new IOException("Parse error at row " + (i + 1) + ": " + e.getMessage());
            }
        }
    }

    public List<Product> search(String query) {
        if (query == null || query.isBlank()) return new ArrayList<>(catalog);
        return catalog.stream()
            .filter(p -> p.matchesQuery(query))
            .collect(Collectors.toList());
    }

    public Optional<Product> findById(String id) {
        return catalog.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public ValidationResult validate(Product p, boolean isNew) {
        ValidationResult result = new ValidationResult();

        if (p.getId() == null || p.getId().isBlank())
            result.addError("id", "ID is required");
        else if (isNew && catalog.stream().anyMatch(x -> x.getId().equals(p.getId())))
            result.addError("id", "ID '" + p.getId() + "' already exists");

        if (p.getName() == null || p.getName().isBlank())
            result.addError("name", "Name is required");

        if (p.getCategory() == null || p.getCategory().isBlank())
            result.addError("category", "Category is required");

        if (p.getPrice() < 0)
            result.addError("price", "Price must be >= 0");

        if (p.getQuantity() < 0)
            result.addError("quantity", "Quantity must be >= 0");

        return result;
    }

    public void addProduct(Product p) throws IOException {
        catalog.add(p);
        save();
    }

    public void updateProduct(Product updated) throws IOException {
        for (int i = 0; i < catalog.size(); i++) {
            System.out.println("Checking product with ID: |" + catalog.get(i).getId() + "|" + " against updated ID: |" + updated.getId() + "|");
            if (catalog.get(i).getId().equals(updated.getId())) {
                catalog.set(i, updated);
                save();
                return;
            }
        }
        throw new IllegalArgumentException("Product not found: " + updated.getId());
    }

    public void deleteProduct(String id) throws IOException {
        boolean removed = catalog.removeIf(p -> p.getId().equals(id));
        if (!removed) throw new IllegalArgumentException("Product not found: " + id);
        save();
    }

    // ---------------------------------------------------------------- persistence

    private void save() throws IOException {
        Serializer s = new Serializer(new String[]{"id","name","category","price","quantity","description","supplier"});
        try {
            for (Product p : catalog) {
                s.push(
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    String.valueOf(p.getPrice()),
                    String.valueOf(p.getQuantity()),
                    p.getDescription(),
                    p.getSupplier()
                );
            }
            s.save(filePath.toString());
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public int size() { return catalog.size(); }

    public Path getFilePath() { return filePath; }
}
