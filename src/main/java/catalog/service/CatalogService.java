package catalog.service;

import catalog.model.Product;
import catalog.model.ValidationResult;
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
            Files.writeString(filePath, "# Merchandise Catalog - pipe-delimited: id|name|category|price|quantity|description|supplier\n");
            return;
        }
        List<String> lines = Files.readAllLines(filePath);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            try {
                catalog.add(Product.fromFileLine(line));
            } catch (Exception e) {
                throw new IOException("Parse error on line " + (i + 1) + ": " + e.getMessage());
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
        List<String> lines = new ArrayList<>();
        lines.add("# Merchandise Catalog - pipe-delimited: id|name|category|price|quantity|description|supplier");
        for (Product p : catalog) {
            lines.add(p.toFileLine());
        }
        Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public int size() { return catalog.size(); }

    public Path getFilePath() { return filePath; }
}
