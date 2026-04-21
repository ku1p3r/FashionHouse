package production;

import common.base.iScreen;
import common.util.Terminal;
import hr.model.Employee;
import java.util.List;

public class MaterialModule implements iScreen {

    private final MaterialRepository repo;
    private final Employee currentUser;

    public MaterialModule(MaterialRepository repo, Employee currentUser) {
        this.repo = repo;
        this.currentUser = currentUser;
    }

    // Entry point: Dashboard
    @Override
    public void run() {
        boolean running = true;
        while (running) {
            Terminal.clearScreen();
            Terminal.printHeader("MATERIAL MANAGEMENT");
            Terminal.printInfo("Logged in as: " + currentUser.getName() + " (" + currentUser.getId() + ")");
            Terminal.println();

            // Low-stock warnings (Alternate Flow 3a/3b)
            List<Material> lowStock = repo.getLowStock();
            if (!lowStock.isEmpty()) {
                Terminal.printWarning("LOW STOCK ALERT: " + lowStock.size() + " material(s) below reorder threshold!");
                for (Material m : lowStock) {
                    Terminal.printWarning("  " + m.getId() + " – " + m.getName()
                            + ": " + m.getQuantity() + " " + m.getUnit()
                            + " (threshold: " + m.getReorderThreshold() + ")");
                }
                Terminal.println();
            }

            printDashboard();
            Terminal.printSubHeader("Material Management Menu");
            Terminal.printMenuOption("(ID/Keyword)", "Browse / Search Materials");
            Terminal.printMenuOption("new", "Add New Material");
            Terminal.printMenuOption("suppliers", "View Supplier Directory");
            Terminal.printMenuOption("back", "Return to Main Menu");
            Terminal.println();

            String choice = Terminal.prompt("Choice:");
            switch (choice) {
                case "new" -> addNewMaterial();
                case "suppliers" -> viewSuppliers();
                case "back" -> running = false;
                default  -> { browseMaterials(choice); }
            }
        }
    }

    // Dashboard summary

    private void printDashboard() {
        Terminal.printSubHeader("Inventory Dashboard");
        List<Material> all = repo.getAll();
        Terminal.printTableHeader("ID", "Name", "Qty", "Unit", "Supplier", "", "Price/Unit");
        int i = 1;
        for (Material m : all) {
            String status = m.isActive() ? (m.isLowStock() ? Terminal.YELLOW + "LOW" + Terminal.RESET : Terminal.GREEN + "OK" + Terminal.RESET) : Terminal.RED + "INACTIVE" + Terminal.RESET;
            Terminal.printTableRow(i++,
                    m.getId(), m.getName(),
                    String.valueOf(m.getQuantity()), m.getUnit(),
                    m.getSupplierName(), "<-- " + status, "$" + String.format("%.2f", m.getPricePerUnit()));
        }
        Terminal.println();
        Terminal.printInfo("Total materials: " + all.size() + "  |  Low stock: " + repo.getLowStock().size());
    }

    // Browse / Search

    private void browseMaterials(String initialQuery) {
        String query = initialQuery == null ? "" : initialQuery;
        while (true) {
            List<Material> results = query.isBlank() ? repo.getAll() : repo.search(query);

            Terminal.clearScreen();
            Terminal.printHeader("BROWSE MATERIALS");
            Terminal.println(Terminal.DIM + "Query: \"" + query + "\"  —  " + results.size() + " result(s)" + Terminal.RESET);
            Terminal.println();

            if (results.isEmpty()) {
                Terminal.printWarning("No matching materials found for: \"" + query + "\"");
                Terminal.println();
                Terminal.printMenuOption("create", "Create a new material using '" + query + "' as the name");
                Terminal.printMenuOption("back", "Back to materials");
                Terminal.println();

                String choice = Terminal.prompt("Choice >");
                if (choice.equalsIgnoreCase("create") || choice.equalsIgnoreCase("new")) {
                    addNewMaterial();
                    query = "";
                    continue;
                }
                return;
            }

            Terminal.printTableHeader("ID", "Name", "Qty", "Unit", "Supplier", "", "Price/Unit");
            for (int i = 0; i < results.size(); i++) {
                Material m = results.get(i);
                boolean low = m.isLowStock();
                String status = m.isActive() ? (m.isLowStock() ? Terminal.YELLOW + "LOW" + Terminal.RESET : Terminal.GREEN + "OK" + Terminal.RESET) : Terminal.RED + "INACTIVE" + Terminal.RESET;

                Terminal.printTableRowColored(low ? Terminal.RED : Terminal.RESET, i + 1,
                        m.getId(), m.getName(),
                        String.valueOf(m.getQuantity()), m.getUnit(),
                        m.getSupplierName(), "<-- " + status, "$" + String.format("%.2f", m.getPricePerUnit()));
            }

            Terminal.println();
            Terminal.printLine();
            Terminal.printMenuOption("(1-" + results.size() + ")", "Enter a result number to view details");
            Terminal.printMenuOption("(ID/Keyword)", "Filter on keyword (press enter to remove filter)");
            Terminal.printMenuOption("new", "Create a new material");
            Terminal.printMenuOption("back", "Return to Material Management");
            Terminal.println();

            String input = Terminal.prompt("Choice >");

            if (input.equalsIgnoreCase("new") || input.equalsIgnoreCase("create")) {
                addNewMaterial();
                query = "";
                continue;
            }
            if (input.equalsIgnoreCase("back")) return;

            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < results.size()) {
                    Material selected = results.get(index);
                    Material fresh = repo.findById(selected.getId());
                    if (fresh == null) fresh = selected;
                    viewMaterialDetail(fresh);
                    // refresh results after possible update
                    // keep same query
                    continue;
                } else {
                    Terminal.printError("Invalid selection.");
                    Terminal.pressEnterToContinue();
                    continue;
                }
            } catch (NumberFormatException e) {
                // treat as new query filter
                query = input;
                continue;
            }
        }
    }

    // Material Detail + Update

    private void viewMaterialDetail(Material m) {
        boolean detailRunning = true;
        while (detailRunning) {
            Terminal.clearScreen();
            Terminal.printHeader("MATERIAL DETAIL");
            Terminal.printField("ID",         m.getId());
            Terminal.printField("Name",       m.getName());
            Terminal.printField("Unit",       m.getUnit());
            Terminal.printField("Stock",      m.getQuantity() + " " + m.getUnit()
                    + (m.isLowStock() ? "  " + Terminal.YELLOW + "[LOW STOCK]" + Terminal.RESET : ""));
            Terminal.printField("Reorder At", String.valueOf(m.getReorderThreshold()) + " " + m.getUnit());
            Terminal.printField("Price/Unit", "$" + String.format("%.2f", m.getPricePerUnit()));
            Terminal.printField("Supplier",   m.getSupplierName() + " (" + m.getSupplierId() + ")");
            Terminal.printField("Status",     m.isActive() ? Terminal.GREEN + "Active" + Terminal.RESET
                                                           : Terminal.RED + "Inactive" + Terminal.RESET);
            Terminal.println();

            Terminal.printSubHeader("Actions");
            Terminal.printMenuOption("1", "Update Stock (restock order)");
            Terminal.printMenuOption("2", "Change Supplier");
            Terminal.printMenuOption("3", "Edit Details (price, threshold, name, unit)");
            Terminal.printMenuOption("4", "Toggle Active Status");
            Terminal.printMenuOption("back", "Return");
            Terminal.println();

            String choice = Terminal.prompt("Choice:");
            switch (choice) {
                case "1" -> new RestockManager(repo).restock(m);
                case "2" -> changeSupplier(m);
                case "3" -> editMaterialDetails(m);
                case "4" -> toggleActive(m);
                case "back" -> detailRunning = false;
                default  -> { Terminal.printError("Invalid option."); Terminal.pressEnterToContinue(); }
            }
        }
    }

    // Restock moved to RestockManager

    // Change Supplier

    private boolean changeSupplier(Material m) {
        Terminal.printSubHeader("Change Supplier for: " + m.getName());

        List<Supplier> active = repo.getActiveSuppliers();
        if (active.isEmpty()) {
            Terminal.printError("No active suppliers available.");
            Terminal.pressEnterToContinue();
            return false;
        }

        Terminal.printTableHeader("ID", "Name", "Email", "Phone");
        int i = 1;
        for (Supplier s : active) {
            Terminal.printTableRow(i++, s.getId(), s.getName().replace(" [INACTIVE]",""),
                    s.getContactEmail(), s.getPhone());
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

    // Edit details

    private void editMaterialDetails(Material m) {
        Terminal.printSubHeader("Edit Material: " + m.getName());

        String name  = Terminal.promptWithDefault("Name:", m.getName());
        String unit  = Terminal.promptWithDefault("Unit:", m.getUnit());

        String priceStr = Terminal.promptWithDefault("Price per unit:", String.valueOf(m.getPricePerUnit()));
        String threshStr = Terminal.promptWithDefault("Reorder threshold:", String.valueOf(m.getReorderThreshold()));

        // Validation
        double price, threshold;
        try {
            price     = Double.parseDouble(priceStr);
            threshold = Double.parseDouble(threshStr);
        } catch (NumberFormatException e) {
            Terminal.printError("Invalid number entered. No changes saved.");
            Terminal.pressEnterToContinue();
            return;
        }

        if (price < 0 || threshold < 0) {
            Terminal.printError("Price and threshold cannot be negative.");
            Terminal.pressEnterToContinue();
            return;
        }

        m.setName(name);
        m.setUnit(unit);
        m.setPricePerUnit(price);
        m.setReorderThreshold(threshold);
        repo.saveMaterials();

        Terminal.printSuccess("Material details updated.");
        Terminal.pressEnterToContinue();
    }

    private void toggleActive(Material m) {
        boolean newState = !m.isActive();
        if (Terminal.confirm("Set material '" + m.getName() + "' to " + (newState ? "Active" : "Inactive") + "?")) {
            m.setActive(newState);
            repo.saveMaterials();
            Terminal.printSuccess("Status updated to: " + (newState ? "Active" : "Inactive"));
        }
        Terminal.pressEnterToContinue();
    }

    // Add New Material

    private void addNewMaterial() {
        Terminal.clearScreen();
        Terminal.printHeader("ADD NEW MATERIAL");

        String name = Terminal.prompt("Material name:");
        if (name.isBlank()) { Terminal.printError("Name cannot be empty."); Terminal.pressEnterToContinue(); return; }

        String unit = Terminal.prompt("Unit of measurement (e.g. meters, kg, pairs):");
        if (unit.isBlank()) { Terminal.printError("Unit cannot be empty."); Terminal.pressEnterToContinue(); return; }

        String qtyStr   = Terminal.promptWithDefault("Initial quantity:", "0");
        String thStr    = Terminal.promptWithDefault("Reorder threshold:", "0");
        String priceStr = Terminal.promptWithDefault("Price per unit ($):", "0");

        double qty, threshold, price;
        try {
            qty       = Double.parseDouble(qtyStr);
            threshold = Double.parseDouble(thStr);
            price     = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Terminal.printError("Invalid number. Material not added.");
            Terminal.pressEnterToContinue();
            return;
        }

        if (qty < 0 || threshold < 0 || price < 0) {
            Terminal.printError("Values cannot be negative.");
            Terminal.pressEnterToContinue();
            return;
        }

        // Select supplier
        Terminal.println();
        Terminal.printSubHeader("Select Supplier");
        List<Supplier> active = repo.getActiveSuppliers();
        Terminal.printTableHeader("ID", "Name", "Email");
        int i = 1;
        for (Supplier s : active) Terminal.printTableRow(i++, s.getId(), s.getName(), s.getContactEmail());
        Terminal.println();

        String supId = Terminal.prompt("Supplier ID:");
        Supplier supplier = repo.findSupplierById(supId);
        if (supplier == null || !supplier.isActive()) {
            Terminal.printError("Supplier not found or inactive.");
            Terminal.pressEnterToContinue();
            return;
        }

        String id = repo.generateMaterialId();
        Material newMaterial = new Material(id, name, unit, qty, threshold, price,
                supplier.getId(), supplier.getName().replace(" [INACTIVE]",""), true);
        repo.addMaterial(newMaterial);
        repo.saveMaterials();

        Terminal.println();
        Terminal.printSuccess("New material added successfully!");
        Terminal.printField("ID",       id);
        Terminal.printField("Name",     name);
        Terminal.printField("Stock",    qty + " " + unit);
        Terminal.printField("Supplier", supplier.getName());
        Terminal.pressEnterToContinue();
    }

    // Supplier Directory
    private void viewSuppliers() {
        SupplierManager mgr = new SupplierManager(repo);
        mgr.manage();
    }
}
