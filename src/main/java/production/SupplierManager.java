package production;

import common.util.Terminal;
import java.util.List;

public class SupplierManager {

    private final MaterialRepository repo;

    public SupplierManager(MaterialRepository repo) {
        this.repo = repo;
    }

    public void manage() {
        boolean running = true;
        while (running) {
            Terminal.clearScreen();
            Terminal.printHeader("SUPPLIER DIRECTORY / MANAGEMENT");

            // Always show current suppliers for easy selection
            showSuppliersInline();

            Terminal.printMenuOption("add", "Add a new supplier");
            Terminal.printMenuOption("edit", "Edit an existing supplier");
            Terminal.printMenuOption("remove", "Remove a supplier");
            Terminal.printMenuOption("back", "Return");
            Terminal.println();

            String choice = Terminal.prompt("Choice:");
            switch (choice.toLowerCase()) {
                case "add"  -> addSupplier();
                case "edit" -> editSupplier();
                case "remove" -> removeSupplier();
                case "back" -> running = false;
                default -> { Terminal.printError("Invalid option."); Terminal.pressEnterToContinue(); }
            }
        }
    }

    private void showSuppliersInline() {
        List<Supplier> all = repo.getAllSuppliers();
        Terminal.printTableHeader("ID", "Name", "Email", "Phone", "Status");
        int i = 1;
        for (Supplier s : all) {
            String status = s.isActive() ? Terminal.GREEN + "Active" + Terminal.RESET : Terminal.RED + "Inactive" + Terminal.RESET;
            Terminal.printTableRow(i++, s.getId(), s.getName().replace(" [INACTIVE]",""), s.getContactEmail(), s.getPhone(), status);
        }
        Terminal.println();
    }

    

    private void addSupplier() {
        Terminal.clearScreen();
        Terminal.printHeader("ADD SUPPLIER");
        String name = Terminal.prompt("Supplier name:");
        if (name.isBlank()) { Terminal.printError("Name cannot be empty."); Terminal.pressEnterToContinue(); return; }
        String email = Terminal.promptWithDefault("Contact email:", "");
        String phone = Terminal.promptWithDefault("Phone:", "");
        boolean active = Terminal.confirm("Set as active supplier?");

        String id = repo.generateSupplierId();
        Supplier s = new Supplier(id, name, email, phone, active);
        repo.addSupplier(s);
        Terminal.printSuccess("Supplier added: " + s.getName());
        Terminal.pressEnterToContinue();
    }

    private void editSupplier() {
        Terminal.printHeader("EDIT SUPPLIER");
        String id = Terminal.prompt("Supplier ID to edit:");
        Supplier s = repo.findSupplierById(id);
        if (s == null) { Terminal.printError("Supplier not found."); Terminal.pressEnterToContinue(); return; }

        String name = Terminal.promptWithDefault("Name:", s.getName().replace(" [INACTIVE]",""));
        String email = Terminal.promptWithDefault("Contact email:", s.getContactEmail());
        String phone = Terminal.promptWithDefault("Phone:", s.getPhone());
        boolean active = Terminal.confirm("Set as active supplier? (current: " + (s.isActive() ? "Active" : "Inactive") + ")");

        Supplier updated = new Supplier(s.getId(), name, email, phone, active);
        repo.updateSupplier(updated);
        Terminal.printSuccess("Supplier updated: " + updated.getName());
        Terminal.pressEnterToContinue();
    }

    private void removeSupplier() {
        Terminal.printHeader("REMOVE SUPPLIER");
        String id = Terminal.prompt("Supplier ID to remove:");
        Supplier s = repo.findSupplierById(id);
        if (s == null) { Terminal.printError("Supplier not found."); Terminal.pressEnterToContinue(); return; }
        if (!Terminal.confirm("Are you sure you want to remove supplier '" + s.getName() + "' ?")) { Terminal.printInfo("Removal cancelled."); Terminal.pressEnterToContinue(); return; }
        repo.removeSupplier(id);
        Terminal.printSuccess("Supplier removed: " + s.getName());
        Terminal.pressEnterToContinue();
    }
}
