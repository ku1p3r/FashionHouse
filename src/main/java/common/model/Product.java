package common.model;

import java.util.Objects;

public class Product {

    private String id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String description;
    private String supplier;

    public Product(String id, String name, String category, double price,
                   int quantity, String description, String supplier) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.supplier = supplier;
    }

    public Product(Product other) {
        this.id = other.id;
        this.name = other.name;
        this.category = other.category;
        this.price = other.price;
        this.quantity = other.quantity;
        this.description = other.description;
        this.supplier = other.supplier;
    }

    public String getId()          { return id; }
    public String getName()        { return name; }
    public String getCategory()    { return category; }
    public double getPrice()       { return price; }
    public int getQuantity()       { return quantity; }
    public String getDescription() { return description; }
    public String getSupplier()    { return supplier; }

    public void setId(String id)               { this.id = id; }
    public void setName(String name)           { this.name = name; }
    public void setCategory(String category)   { this.category = category; }
    public void setPrice(double price)         { this.price = price; }
    public void setQuantity(int quantity)      { this.quantity = quantity; }
    public void setDescription(String description) { this.description = description; }
    public void setSupplier(String supplier)   { this.supplier = supplier; }

    public boolean matchesQuery(String query) {
        if (query == null || query.isBlank()) return true;
        String q = query.toLowerCase();
        return id.toLowerCase().contains(q)
            || name.toLowerCase().contains(q)
            || category.toLowerCase().contains(q)
            || description.toLowerCase().contains(q)
            || supplier.toLowerCase().contains(q);
    }

    /** Serialize to a single pipe-delimited line */
    public String toFileLine() {
        return String.join("|",
            escape(id),
            escape(name),
            escape(category),
            String.valueOf(price),
            String.valueOf(quantity),
            escape(description),
            escape(supplier)
        );
    }

    /** Parse a pipe-delimited line into a Product */
    public static Product fromFileLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 7) {
            throw new IllegalArgumentException("Invalid product line: " + line);
        }
        return new Product(
            unescape(parts[0]),
            unescape(parts[1]),
            unescape(parts[2]),
            Double.parseDouble(parts[3]),
            Integer.parseInt(parts[4]),
            unescape(parts[5]),
            unescape(parts[6])
        );
    }

    private static String escape(String s)   { return s.replace("|", "\\|"); }
    private static String unescape(String s) { return s.replace("\\|", "|"); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        return Objects.equals(id, ((Product) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    public String toString() {
        return toFileLine();
    }
}
