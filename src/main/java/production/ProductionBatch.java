package production;

import common.base.Selectable;

public class ProductionBatch implements Selectable {

    public enum Status { PENDING, IN_PROGRESS, COMPLETED, CANCELLED }

    private String id;
    private String productId;
    private String productName;
    private String requestedBy;   // employee ID
    private int batchSize;
    private Status status;
    private String materialUsage; // e.g. "M-001:40.0,M-002:5.0"
    private String createdDate;

    public ProductionBatch(String id, String productId, String productName,
                           String requestedBy, int batchSize, Status status,
                           String materialUsage, String createdDate) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.requestedBy = requestedBy;
        this.batchSize = batchSize;
        this.status = status;
        this.materialUsage = materialUsage;
        this.createdDate = createdDate;
    }

    @Override public String getId()   { return id; }
    @Override public String getName() { return id + " – " + productName + " (x" + batchSize + ") [" + status + "]"; }

    public String getProductId()     { return productId; }
    public String getProductName()   { return productName; }
    public String getRequestedBy()   { return requestedBy; }
    public int getBatchSize()        { return batchSize; }
    public Status getStatus()        { return status; }
    public String getMaterialUsage() { return materialUsage; }
    public String getCreatedDate()   { return createdDate; }

    public void setStatus(Status status)             { this.status = status; }
    public void setMaterialUsage(String usage)       { this.materialUsage = usage; }
    public void setBatchSize(int size)               { this.batchSize = size; }

    /** Returns a map representation of material usage: materialId -> quantity */
    public java.util.Map<String, Double> parseMaterialUsage() {
        java.util.Map<String, Double> map = new java.util.LinkedHashMap<>();
        if (materialUsage == null || materialUsage.isBlank()) return map;
        for (String entry : materialUsage.split(",")) {
            String[] parts = entry.split(":");
            if (parts.length == 2) {
                try { map.put(parts[0].trim(), Double.parseDouble(parts[1].trim())); }
                catch (NumberFormatException ignored) {}
            }
        }
        return map;
    }

    public static String statusLabel(Status s) {
        return switch (s) {
            case PENDING     -> "\u001B[33mPENDING\u001B[0m";
            case IN_PROGRESS -> "\u001B[36mIN PROGRESS\u001B[0m";
            case COMPLETED   -> "\u001B[32mCOMPLETED\u001B[0m";
            case CANCELLED   -> "\u001B[31mCANCELLED\u001B[0m";
        };
    }
}
