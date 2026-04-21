package production;

import common.util.Terminal;
import java.util.List;

public class RestockManager {

    private final MaterialRepository repo;

    public RestockManager(MaterialRepository repo) {
        this.repo = repo;
    }

    public void restock(Material m) {
        Terminal.printSubHeader("Restock: " + m.getName());

        // Ensure supplier is active
        Supplier supplier = repo.findSupplierById(m.getSupplierId());
        if (supplier == null || !supplier.isActive()) {
            Terminal.printError("Current supplier '" + m.getSupplierName() + "' is inactive or unavailable!");
            Terminal.printInfo("Please select an alternative supplier before restocking.");
            boolean changed = changeSupplierInteractive(m);
            if (!changed) return;
        }

        String qtyInput = Terminal.prompt("Quantity to order (current: " + m.getQuantity() + " " + m.getUnit() + "): ");

        double qty;
        try {
            qty = Double.parseDouble(qtyInput);
        } catch (NumberFormatException e) {
            Terminal.printError("Invalid quantity. Must be a number.");
            Terminal.pressEnterToContinue();
            return;
        }

        if (qty <= 0) {
            Terminal.printError("Quantity must be greater than zero.");
            Terminal.pressEnterToContinue();
            return;
        }
        if (qty < m.getReorderThreshold()) {
            Terminal.printError("Quantity must be greater than the reorder threshold.");
            Terminal.pressEnterToContinue();
            return;
        }

        double newQty = m.getQuantity() + qty;
        double totalCost = qty * m.getPricePerUnit();

        Terminal.println();
        Terminal.printInfo("Order Summary:");
        Terminal.printField("Material",   m.getName());
        Terminal.printField("Supplier",   m.getSupplierName());
        Terminal.printField("Order Qty",  qty + " " + m.getUnit());
        Terminal.printField("Unit Price", "$" + String.format("%.2f", m.getPricePerUnit()));
        Terminal.printField("Total Cost", "$" + String.format("%.2f", totalCost));
        Terminal.printField("New Stock",  newQty + " " + m.getUnit());
        Terminal.println();

        if (!Terminal.confirm("Confirm restock order?")) {
            Terminal.printInfo("Restock cancelled.");
            Terminal.pressEnterToContinue();
            return;
        }

        m.setQuantity(newQty);
        repo.saveMaterials();

        Terminal.printSuccess("Restock recorded. New stock level: " + newQty + " " + m.getUnit());

        if (m.isLowStock()) {
            Terminal.printWarning("Stock is still below reorder threshold after restocking!");
        }

        Terminal.pressEnterToContinue();
    }

    // Interactive supplier selection used when restocking
    private boolean changeSupplierInteractive(Material m) {
        List<Supplier> active = repo.getActiveSuppliers();
        if (active.isEmpty()) {
            Terminal.printError("No active suppliers available.");
            Terminal.pressEnterToContinue();
            return false;
        }

        Terminal.printTableHeader("ID", "Name", "Email", "Phone");
        int i = 1;
        for (Supplier s : active) {
            Terminal.printTableRow(i++, s.getId(), s.getName().replace(" [INACTIVE]",""), s.getContactEmail(), s.getPhone());
        }
        Terminal.println();

        String sel = Terminal.prompt("Enter Supplier ID to select (or 'back'):");
        if (sel.equalsIgnoreCase("back")) return false;

        Supplier chosen = repo.findSupplierById(sel);
        if (chosen == null || !chosen.isActive()) {
            Terminal.printError("Supplier not found or inactive.");
            Terminal.pressEnterToContinue();
            return false;
        }

        m.setSupplierId(chosen.getId());
        m.setSupplierName(chosen.getName().replace(" [INACTIVE]",""));
        repo.saveMaterials();

        Terminal.printSuccess("Supplier updated to: " + chosen.getName());
        Terminal.pressEnterToContinue();
        return true;
    }
}
