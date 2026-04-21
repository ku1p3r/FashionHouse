package production;

import common.util.Terminal;
import analytics.services.AnalyticsService;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import production.ProductionBatch.Status;

public class ProductionModule {

    private final ProductionRepository prodRepo;
    private final MaterialRepository   matRepo;
    private final Employee             currentUser;

    public ProductionModule(ProductionRepository prodRepo,
                            MaterialRepository   matRepo,
                            Employee             currentUser) {
        this.prodRepo    = prodRepo;
        this.matRepo     = matRepo;
        this.currentUser = currentUser;
    }

    // Entry point

    public void run() {
        boolean running = true;
        while (running) {
            Terminal.clearScreen();
            Terminal.printHeader("PRODUCTION MANAGEMENT");
            Terminal.printInfo("Logged in as: " + currentUser.getName() + " (" + currentUser.getId() + ")");
            Terminal.println();

            printActiveBatchSummary();

            Terminal.printSubHeader("Production Menu");
            Terminal.printMenuOption("1", "View All Batches");
            Terminal.printMenuOption("2", "Create New Production Batch");
            Terminal.printMenuOption("3", "Update Batch Status");
            Terminal.printMenuOption("back", "Return to Main Menu");
            Terminal.println();

            String choice = Terminal.prompt("Choice:");
            switch (choice) {
                case "1"    -> viewAllBatches();
                case "2"    -> createBatch("");
                case "3"    -> updateBatchStatus();
                case "back" -> running = false;
                default     -> { Terminal.printError("Invalid option."); Terminal.pressEnterToContinue(); }
            }
        }
    }

    // Active batch summary on dashboard

    private void printActiveBatchSummary() {
        List<ProductionBatch> active = prodRepo.getActive();
        if (active.isEmpty()) {
            Terminal.printInfo("No active production batches.");
            Terminal.println();
            return;
        }
        Terminal.printSubHeader("Active Batches (" + active.size() + ")");
        Terminal.printTableHeader("ID", "Product", "Size", "Requested By", "Status");
        int i = 1;
        for (ProductionBatch b : active) {
            Terminal.printTableRow(i++, b.getId(), b.getProductName(),
                    String.valueOf(b.getBatchSize()), b.getRequestedBy(),
                    b.getStatus().name());
        }
        Terminal.println();
    }

    // View all batches

    private void viewAllBatches() {
        Terminal.clearScreen();
        Terminal.printHeader("ALL PRODUCTION BATCHES");

        List<ProductionBatch> all = prodRepo.getAll();
        if (all.isEmpty()) {
            Terminal.printInfo("No production batches on record.");
            Terminal.pressEnterToContinue();
            return;
        }

        Terminal.printTableHeader("ID", "Product", "Size", "Status", "Date", "Requested By");
        int i = 1;
        for (ProductionBatch b : all) {
            Terminal.printTableRow(i++,
                    b.getId(), b.getProductName(),
                    String.valueOf(b.getBatchSize()),
                    b.getStatus().name(),
                    b.getCreatedDate(),
                    b.getRequestedBy());
        }
        Terminal.println();

        String sel = Terminal.prompt("Enter Batch ID for details (or 'back'):");
        if (!sel.equalsIgnoreCase("back")) {
            ProductionBatch batch = prodRepo.findById(sel);
            if (batch == null) {
                Terminal.printError("Batch not found.");
            } else {
                viewBatchDetail(batch);
                return;
            }
        }
        Terminal.pressEnterToContinue();
    }

    // Batch detail view

    private void viewBatchDetail(ProductionBatch b) {
        Terminal.clearScreen();
        Terminal.printHeader("BATCH DETAIL: " + b.getId());
        Terminal.printField("Batch ID",    b.getId());
        Terminal.printField("Product",     b.getProductName() + " (" + b.getProductId() + ")");
        Terminal.printField("Batch Size",  String.valueOf(b.getBatchSize()));
        Terminal.printField("Status",      ProductionBatch.statusLabel(b.getStatus()));
        Terminal.printField("Requested By",b.getRequestedBy());
        Terminal.printField("Created",     b.getCreatedDate());
        Terminal.println();

        Terminal.printSubHeader("Material Usage Plan");
        Map<String, Double> usage = b.parseMaterialUsage();
        if (usage.isEmpty()) {
            Terminal.printInfo("No material usage recorded.");
        } else {
            Terminal.printTableHeader("Material ID", "Name", "Required Qty");
            int i = 1;
            for (Map.Entry<String, Double> entry : usage.entrySet()) {
                Material mat = matRepo.findById(entry.getKey());
                String matName = mat != null ? mat.getName() : "(unknown)";
                Terminal.printTableRow(i++, entry.getKey(), matName,
                        String.format("%.2f", entry.getValue()));
            }
        }
        Terminal.println();
        Terminal.pressEnterToContinue();
    }

    // Create new production batch

    public void createBatch(String productId) {
        Terminal.clearScreen();
        Terminal.printHeader("CREATE PRODUCTION BATCH");

        // Step 1: Select product
        Terminal.printSubHeader("Step 1 of 3 – Select Product");
        List<String[]> products = prodRepo.getProductList();
        if (products.isEmpty()) {
            Terminal.printError("No products found in catalog.");
            Terminal.pressEnterToContinue();
            return;
        }

        if (productId.isEmpty()) {
            Terminal.printTableHeader("ID", "Name", "Current Qty");
            int i = 1;
            for (String[] p : products) Terminal.printTableRow(i++, p[0], p[1], p[2]);
            Terminal.println();
            productId = Terminal.prompt("Enter Product ID to produce (or 'back'):");
            if (productId.equalsIgnoreCase("back")) return;
        } else {
            Terminal.printInfo("Restocking product: " + productId);
            Terminal.println();
        }

        String[] selectedProduct = null;
        for (String[] p : products) {
            if (p[0].equalsIgnoreCase(productId)) { selectedProduct = p; break; }
        }
        if (selectedProduct == null) {
            Terminal.printError("Product not found.");
            Terminal.pressEnterToContinue();
            return;
        }

        // Step 2: Set batch size
        Terminal.printSubHeader("Step 2 of 3 – Batch Size");
        Terminal.printInfo("Product: " + selectedProduct[1] + "  |  Current stock: " + selectedProduct[2]);
        String sizeStr = Terminal.prompt("How many units to produce?");
        int batchSize;
        try {
            batchSize = Integer.parseInt(sizeStr);
        } catch (NumberFormatException e) {
            Terminal.printError("Invalid number.");
            Terminal.pressEnterToContinue();
            return;
        }
        if (batchSize <= 0) {
            Terminal.printError("Batch size must be greater than zero.");
            Terminal.pressEnterToContinue();
            return;
        }

        // Step 3: Determine material requirements (use product definition if available)
        Terminal.printSubHeader("Step 3 of 3 – Material Requirements");
        String perUnitMaterials = prodRepo.getProductMaterials(productId);

        Map<String, Double> materialUsage = new LinkedHashMap<>();
        boolean insufficientFlag = false;

        if (perUnitMaterials != null && !perUnitMaterials.isBlank()) {
            // parse per-unit materials (format: M-001:2.0,M-002:0.5)
            String[] entries = perUnitMaterials.split(",");
            for (String e : entries) {
                String[] parts = e.split(":");
                if (parts.length == 2) {
                    try {
                        double perUnit = Double.parseDouble(parts[1].trim());
                        materialUsage.put(parts[0].trim(), perUnit * batchSize);
                    } catch (NumberFormatException ignored) {}
                }
            }
            Terminal.printSubHeader("Materials required for " + batchSize + " units (calculated from product definition)");
            Terminal.printTableHeader("Material ID", "Name", "Required", "Available", "Unit");
            int k = 1;
            for (Map.Entry<String, Double> e2 : materialUsage.entrySet()) {
                Material mat = matRepo.findById(e2.getKey());
                String matName = mat != null ? mat.getName() : "(unknown)";
                String requiredStr = String.format("%.2f", e2.getValue());
                String availableStr = mat != null ? String.format("%.2f", mat.getQuantity()) : "N/A";
                String unit = mat != null ? mat.getUnit() : "";
                Terminal.printTableRow(k++, e2.getKey(), matName,
                        requiredStr + " " + unit,
                        availableStr + (unit.isEmpty() ? "" : " " + unit));
            }
            Terminal.println();
            if (!Terminal.confirm("Use these material requirements?")) {
                Terminal.printInfo("Batch creation cancelled.");
                Terminal.pressEnterToContinue();
                return;
            }
        } else {
            // fallback to manual entry if product has no materials defined
            Terminal.printInfo("No materials defined for this product. Please specify materials manually.");
            Terminal.printInfo("Type 'done' when finished, 'back' to cancel.");
            Terminal.println();

            while (true) {
                // Show available materials
                List<Material> allMats = matRepo.getAll();
                Terminal.printTableHeader("ID", "Name", "Available", "Unit");
                int j = 1;
                for (Material m : allMats) {
                    String avail = m.isLowStock()
                            ? Terminal.YELLOW + String.format("%.2f", m.getQuantity()) + Terminal.RESET
                            : String.format("%.2f", m.getQuantity());
                    Terminal.printTableRow(j++, m.getId(), m.getName(), avail, m.getUnit());
                }
                Terminal.println();

                String matId = Terminal.prompt("Material ID (or 'done' / 'back'):");
                if (matId.equalsIgnoreCase("back")) return;
                if (matId.equalsIgnoreCase("done")) break;

                Material mat = matRepo.findById(matId);
                if (mat == null) {
                    Terminal.printError("Material not found: " + matId);
                    continue;
                }

                String qtyStr = Terminal.prompt("Total quantity of " + mat.getName() + " needed (" + mat.getUnit() + "):");
                double needed;
                try {
                    needed = Double.parseDouble(qtyStr);
                } catch (NumberFormatException ex) {
                    Terminal.printError("Invalid quantity.");
                    continue;
                }
                if (needed <= 0) {
                    Terminal.printError("Quantity must be greater than zero.");
                    continue;
                }

                // Alt Flow 8c: Check material availability
                if (mat.getQuantity() < needed) {
                    Terminal.printWarning("Insufficient stock for " + mat.getName() + "!");
                    Terminal.printField("  Required",  needed + " " + mat.getUnit());
                    Terminal.printField("  Available", mat.getQuantity() + " " + mat.getUnit());
                    Terminal.printField("  Shortage",  (needed - mat.getQuantity()) + " " + mat.getUnit());
                    Terminal.println();
                    Terminal.printInfo("Options:");
                    Terminal.printMenuOption("1", "Add to batch anyway (batch will be PENDING until restocked)");
                    Terminal.printMenuOption("2", "Skip this material");
                    Terminal.printMenuOption("3", "Cancel batch creation");
                    String action = Terminal.prompt("Choice:");
                    if (action.equals("2")) continue;
                    if (action.equals("3")) return;
                    insufficientFlag = true; // batch will be PENDING
                }

                materialUsage.put(mat.getId(), needed);
                Terminal.printSuccess("Added: " + mat.getName() + " x " + needed + " " + mat.getUnit());
                Terminal.println();
            }
        }

        if (materialUsage.isEmpty()) {
            Terminal.printError("No materials specified. Batch not created.");
            Terminal.pressEnterToContinue();
            return;
        }

        // Build usage string
        StringBuilder usageStr = new StringBuilder();
        for (Map.Entry<String, Double> e : materialUsage.entrySet()) {
            if (usageStr.length() > 0) usageStr.append(",");
            usageStr.append(e.getKey()).append(":").append(e.getValue());
        }

        // Confirm
        Terminal.println();
        Terminal.printSubHeader("Confirm New Batch");
        Terminal.printField("Product",    selectedProduct[1] + " (" + productId + ")");
        Terminal.printField("Batch Size", String.valueOf(batchSize));
        Terminal.printField("Materials",  usageStr.toString());
        Terminal.printField("Status",     insufficientFlag ? "PENDING (insufficient materials)" : "PENDING");
        Terminal.printField("Assigned To","HR / " + currentUser.getId());
        Terminal.println();

        if (!Terminal.confirm("Create this production batch?")) {
            Terminal.printInfo("Batch creation cancelled.");
            Terminal.pressEnterToContinue();
            return;
        }

        String id    = prodRepo.generateBatchId();
        String today = LocalDate.now().toString();
        ProductionBatch batch = new ProductionBatch(
                id, productId, selectedProduct[1],
                currentUser.getId(), batchSize,
                Status.PENDING, usageStr.toString(), today);

        prodRepo.addBatch(batch);
        prodRepo.saveBatches();

        Terminal.println();
        Terminal.printSuccess("Production batch created: " + id);
        if (insufficientFlag) {
            Terminal.printWarning("Batch is PENDING due to insufficient materials. Restock before advancing.");
        }
        Terminal.pressEnterToContinue();
    }

    // =========================================================================
    // Update batch status (advance workflow / complete production)
    // =========================================================================

    private void updateBatchStatus() {
        Terminal.clearScreen();
        Terminal.printHeader("UPDATE BATCH STATUS");

        List<ProductionBatch> all = prodRepo.getAll();
        if (all.isEmpty()) {
            Terminal.printInfo("No batches found.");
            Terminal.pressEnterToContinue();
            return;
        }

        Terminal.printTableHeader("ID", "Product", "Size", "Status", "Date");
        int i = 1;
        for (ProductionBatch b : all) {
            Terminal.printTableRow(i++, b.getId(), b.getProductName(),
                    String.valueOf(b.getBatchSize()), b.getStatus().name(), b.getCreatedDate());
        }
        Terminal.println();

        String sel = Terminal.prompt("Enter Batch ID to update (or 'back'):");
        if (sel.equalsIgnoreCase("back")) return;

        ProductionBatch batch = prodRepo.findById(sel);
        if (batch == null) {
            Terminal.printError("Batch not found.");
            Terminal.pressEnterToContinue();
            return;
        }

        advanceBatchStatus(batch);
    }

    // -------------------------------------------------------------------------
    // Status advancement logic
    // -------------------------------------------------------------------------

    private void advanceBatchStatus(ProductionBatch batch) {
        Terminal.printSubHeader("Batch: " + batch.getId() + " – " + batch.getProductName());
        Terminal.printField("Current Status", ProductionBatch.statusLabel(batch.getStatus()));
        Terminal.println();

        ProductionBatch.Status current = batch.getStatus();

        if (current == ProductionBatch.Status.COMPLETED || current == ProductionBatch.Status.CANCELLED) {
            Terminal.printInfo("This batch is already " + current.name() + " and cannot be changed.");
            Terminal.pressEnterToContinue();
            return;
        }

        Terminal.printSubHeader("Update Status");
        ProductionBatch.Status next = current == ProductionBatch.Status.PENDING ? ProductionBatch.Status.IN_PROGRESS : ProductionBatch.Status.COMPLETED;
        Terminal.printMenuOption("1", "Advance to " + next.name());
        Terminal.printMenuOption("2", "Cancel this batch");
        Terminal.printMenuOption("back", "Return");
        Terminal.println();

        String choice = Terminal.prompt("Choice:");
        switch (choice) {
            case "1" -> advanceTo(batch, next);
            case "2" -> cancelBatch(batch);
            case "back" -> {}
            default  -> Terminal.printError("Invalid option.");
        }
    }

    private void advanceTo(ProductionBatch batch, ProductionBatch.Status next) {
        if (next == ProductionBatch.Status.IN_PROGRESS) {
            // Validate all materials are available before starting
            boolean canStart = checkMaterialAvailability(batch);
            if (!canStart) {
                Terminal.printWarning("Cannot advance to IN PROGRESS – insufficient materials.");
                Terminal.printInfo("Please restock materials in Material Management first.");
                Terminal.pressEnterToContinue();
                return;
            }
        }

        if (next == ProductionBatch.Status.COMPLETED) {
            completeProduction(batch);
            return;
        }

        if (!Terminal.confirm("Advance batch " + batch.getId() + " to " + next.name() + "?")) {
            Terminal.pressEnterToContinue();
            return;
        }

        batch.setStatus(next);
        prodRepo.saveBatches();
        Terminal.printSuccess("Batch status updated to: " + next.name());
        Terminal.pressEnterToContinue();
    }

    // -------------------------------------------------------------------------
    // Check material availability before advancing
    // -------------------------------------------------------------------------

    private boolean checkMaterialAvailability(ProductionBatch batch) {
        Map<String, Double> usage = batch.parseMaterialUsage();
        boolean allOk = true;

        Terminal.printSubHeader("Material Availability Check");
        Terminal.printTableHeader("Material", "Required", "Available", "Status");
        int i = 1;
        for (Map.Entry<String, Double> entry : usage.entrySet()) {
            Material mat = matRepo.findById(entry.getKey());
            if (mat == null) {
                Terminal.printTableRow(i++, entry.getKey(), String.format("%.2f", entry.getValue()),
                        "NOT FOUND", Terminal.RED + "FAIL" + Terminal.RESET);
                allOk = false;
                continue;
            }
            boolean ok     = mat.getQuantity() >= entry.getValue();
            String status  = ok ? Terminal.GREEN + "OK" + Terminal.RESET
                               : Terminal.RED + "SHORTAGE" + Terminal.RESET;
            Terminal.printTableRow(i++, mat.getName(),
                    String.format("%.2f %s", entry.getValue(), mat.getUnit()),
                    String.format("%.2f %s", mat.getQuantity(), mat.getUnit()),
                    status);
            if (!ok) allOk = false;
        }
        Terminal.println();
        return allOk;
    }

    // -------------------------------------------------------------------------
    // Complete production: deduct materials, add to product catalog
    // -------------------------------------------------------------------------

    private void completeProduction(ProductionBatch batch) {
        Terminal.printSubHeader("Complete Production: " + batch.getId());

        // Final material check
        boolean canComplete = checkMaterialAvailability(batch);
        if (!canComplete) {
            Terminal.printWarning("Cannot complete – material shortage detected.");
            Terminal.printInfo("Restock materials and try again.");
            Terminal.pressEnterToContinue();
            return;
        }

        Terminal.printInfo("Completing production will:");
        Terminal.println("  • Deduct required materials from inventory");
        Terminal.println("  • Add " + batch.getBatchSize() + " unit(s) of '"
                + batch.getProductName() + "' to the product catalog");
        Terminal.println();

        if (!Terminal.confirm("Confirm production completion?")) {
            Terminal.printInfo("Completion cancelled.");
            Terminal.pressEnterToContinue();
            return;
        }

        // Deduct materials
        Map<String, Double> usage = batch.parseMaterialUsage();
        for (Map.Entry<String, Double> entry : usage.entrySet()) {
            Material mat = matRepo.findById(entry.getKey());
            if (mat != null) {
                double newQty = mat.getQuantity() - entry.getValue();
                mat.setQuantity(Math.max(0, newQty));
                Terminal.printInfo("Deducted " + entry.getValue() + " "
                        + mat.getUnit() + " of " + mat.getName()
                        + " (remaining: " + String.format("%.2f", mat.getQuantity()) + ")");
            }
        }
        matRepo.saveMaterials();

        // Add finished goods to product catalog
        prodRepo.updateProductQuantity(batch.getProductId(), batch.getBatchSize());

        // Mark batch complete
        batch.setStatus(Status.COMPLETED);
        prodRepo.saveBatches();

        Terminal.println();
        Terminal.printSuccess("Production COMPLETED for batch " + batch.getId() + "!");
        Terminal.printSuccess("Added " + batch.getBatchSize() + " unit(s) of '"
                + batch.getProductName() + "' to product catalog.");

        // Offer to log stocked units to retailer analytics (uses AnalyticsService.stock)
        try {
            AnalyticsService analyticsService = new AnalyticsService();
            if (Terminal.confirm("Log stocked units to retailer analytics?")) {
                String storeName = Terminal.prompt("Store name (e.g. fashionstore1):");
                LocalDate now = LocalDate.now();
                int month = now.getMonthValue();
                int year = now.getYear();
                analyticsService.stock(storeName, batch.getProductName(), month, year, batch.getBatchSize());
                Terminal.printSuccess("Logged " + batch.getBatchSize() + " units to analytics for store " + storeName + ".");
            }
        } catch (Exception e) {
            Terminal.printError("Failed to log analytics: " + e.getMessage());
        }

        // Alt Flow 14a: Check for newly low stock after material deduction
        List<Material> lowStock = matRepo.getLowStock();
        if (!lowStock.isEmpty()) {
            Terminal.println();
            Terminal.printWarning("Low stock triggered after production:");
            for (Material m : lowStock) {
                Terminal.printWarning("  " + m.getId() + " – " + m.getName()
                        + ": " + String.format("%.2f", m.getQuantity())
                        + " " + m.getUnit()
                        + " (threshold: " + m.getReorderThreshold() + ")");
            }
            Terminal.printInfo("Visit Material Management to restock.");
        }

        Terminal.pressEnterToContinue();
    }

    // -------------------------------------------------------------------------
    // Cancel batch
    // -------------------------------------------------------------------------

    private void cancelBatch(ProductionBatch batch) {
        if (!Terminal.confirm("Cancel batch " + batch.getId() + "? This cannot be undone.")) {
            Terminal.pressEnterToContinue();
            return;
        }
        batch.setStatus(Status.CANCELLED);
        prodRepo.saveBatches();
        Terminal.printSuccess("Batch " + batch.getId() + " has been cancelled.");
        Terminal.pressEnterToContinue();
    }
}
