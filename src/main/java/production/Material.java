package production;

import common.base.Selectable;

public class Material implements Selectable {
    private String id;
    private String name;
    private String unit;
    private double quantity;
    private double reorderThreshold;
    private double pricePerUnit;
    private String supplierId;
    private String supplierName;
    private boolean active;

    public Material(String id, String name, String unit, double quantity,
                    double reorderThreshold, double pricePerUnit,
                    String supplierId, String supplierName, boolean active) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
        this.reorderThreshold = reorderThreshold;
        this.pricePerUnit = pricePerUnit;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.active = active;
    }

    @Override public String getId()   { return id; }
    @Override public String getName() { return name; }

    public String getUnit()              { return unit; }
    public double getQuantity()          { return quantity; }
    public double getReorderThreshold()  { return reorderThreshold; }
    public double getPricePerUnit()      { return pricePerUnit; }
    public String getSupplierId()        { return supplierId; }
    public String getSupplierName()      { return supplierName; }
    public boolean isActive()           { return active; }
    public boolean isLowStock()         { return quantity <= reorderThreshold; }

    public void setQuantity(double qty)          { this.quantity = qty; }
    public void setSupplierId(String id)         { this.supplierId = id; }
    public void setSupplierName(String name)     { this.supplierName = name; }
    public void setPricePerUnit(double price)    { this.pricePerUnit = price; }
    public void setReorderThreshold(double t)    { this.reorderThreshold = t; }
    public void setActive(boolean active)        { this.active = active; }
    public void setName(String name)             { this.name = name; }
    public void setUnit(String unit)             { this.unit = unit; }

    @Override
    public String toString() {
        return String.format("%s | %s | %.2f %s | Supplier: %s%s",
                id, name, quantity, unit, supplierName,
                isLowStock() ? " [LOW STOCK]" : "");
    }
}
