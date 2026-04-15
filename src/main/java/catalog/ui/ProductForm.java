package catalog.ui;

import catalog.model.ValidationResult;
import catalog.service.CatalogService;
import common.model.Product;
import common.util.Terminal;
import java.util.UUID;

/**
 * Inline edit panel for creating or updating a product.
 * Returns null if the user cancels.
 */
public class ProductForm {

    public enum Mode { CREATE, EDIT }

    private final CatalogService service;

    public ProductForm(CatalogService service) {
        this.service = service;
    }

    /**
     * Run the form. Returns a filled Product on confirm, null on cancel.
     */
    public Product run(Product existing, Mode mode, String prefillName) {
        Product draft = existing != null ? new Product(existing) : blankProduct(prefillName);

        ValidationResult lastValidation = new ValidationResult();
        boolean firstRun = true;

        while (true) {
            Terminal.clearScreen();
            Terminal.printHeader(mode == Mode.CREATE ? "New Product" : "Edit Product");

            if (!firstRun && !lastValidation.isValid()) {
                Terminal.printError("Please fix the errors highlighted below.");
                Terminal.println();
            }
            firstRun = false;

            // ── Field group: Identity ──────────────────────────────────────
            Terminal.printSubHeader("Identity");
            String idInput = Terminal.promptWithError(
                    "ID          :", draft.getId(), lastValidation.getError("id"));
            if (isCancelSignal(idInput)) return null;

            String nameInput = Terminal.promptWithError(
                    "Name        :", draft.getName(), lastValidation.getError("name"));
            if (isCancelSignal(nameInput)) return null;

            // ── Field group: Classification ────────────────────────────────
            Terminal.printSubHeader("Classification");
            String categoryInput = Terminal.promptWithError(
                    "Category    :", draft.getCategory(), lastValidation.getError("category"));
            if (isCancelSignal(categoryInput)) return null;

            String supplierInput = Terminal.promptWithError(
                    "Supplier    :", draft.getSupplier(), lastValidation.getError("supplier"));
            if (isCancelSignal(supplierInput)) return null;

            // ── Field group: Pricing & Stock ───────────────────────────────
            Terminal.printSubHeader("Pricing & Stock");
            String priceRaw = Terminal.promptWithError(
                    "Price ($)   :", String.valueOf(draft.getPrice()), lastValidation.getError("price"));
            if (isCancelSignal(priceRaw)) return null;

            String qtyRaw = Terminal.promptWithError(
                    "Quantity    :", String.valueOf(draft.getQuantity()), lastValidation.getError("quantity"));
            if (isCancelSignal(qtyRaw)) return null;

            // ── Field group: Details ───────────────────────────────────────
            Terminal.printSubHeader("Details");
            String descInput = Terminal.promptWithError(
                    "Description :", draft.getDescription(), lastValidation.getError("description"));
            if (isCancelSignal(descInput)) return null;

                String materialsInput = Terminal.promptWithDefault(
                    "Materials (format: M-001:2.0,M-002:0.5)", draft.getMaterials() == null ? "" : draft.getMaterials());
                if (isCancelSignal(materialsInput)) return null;

            // ── Parse numerics ─────────────────────────────────────────────
            double price = draft.getPrice();
            int quantity = draft.getQuantity();

            ValidationResult parseErrors = new ValidationResult();
            try { price = Double.parseDouble(priceRaw); }
            catch (NumberFormatException e) { parseErrors.addError("price", "Must be a valid number (e.g. 9.99)"); }
            try { quantity = Integer.parseInt(qtyRaw); }
            catch (NumberFormatException e) { parseErrors.addError("quantity", "Must be a whole number (e.g. 42)"); }

            if (!parseErrors.isValid()) {
                lastValidation = parseErrors;
                // Rebuild draft with whatever parsed OK so fields are pre-filled
                draft.setName(nameInput);
                draft.setCategory(categoryInput);
                draft.setSupplier(supplierInput);
                draft.setDescription(descInput);
                continue;
            }

            // ── Update draft ───────────────────────────────────────────────
            draft.setId(idInput);
            draft.setName(nameInput);
            draft.setCategory(categoryInput);
            draft.setSupplier(supplierInput);
            draft.setPrice(price);
            draft.setQuantity(quantity);
            draft.setDescription(descInput);
            draft.setMaterials(materialsInput);

            // ── Validate business rules ────────────────────────────────────
            lastValidation = service.validate(draft, mode == Mode.CREATE);
            if (!lastValidation.isValid()) {
                continue;
            }

            // ── Confirm ────────────────────────────────────────────────────
            Terminal.println();
            Terminal.printLine();
            printDraftSummary(draft);
            Terminal.printLine();
            Terminal.println();
            Terminal.printMenuOption("confirm", "Confirm & save");
            Terminal.printMenuOption("edit", "Edit again");
            Terminal.printMenuOption("cancel", "Cancel");
            Terminal.println();

            while (true) {
                String choice = Terminal.prompt("Choice >");
                if (choice.equalsIgnoreCase("confirm")) return draft;
                if (choice.equalsIgnoreCase("cancel")) return null;
                if (choice.equalsIgnoreCase("edit")) break;
                Terminal.printError("Invalid option. Please choose 'confirm', 'edit', or 'cancel'.");
            }
        }
    }

    private void printDraftSummary(Product p) {
        Terminal.printField("ID",          p.getId());
        Terminal.printField("Name",        p.getName());
        Terminal.printField("Category",    p.getCategory());
        Terminal.printField("Supplier",    p.getSupplier());
        Terminal.printField("Price",       String.format("$%.2f", p.getPrice()));
        Terminal.printField("Quantity",    String.valueOf(p.getQuantity()));
        Terminal.printField("Description", p.getDescription());
        Terminal.printField("Materials",   p.getMaterials() == null ? "" : p.getMaterials());
    }

    private Product blankProduct(String prefillName) {
        return new Product(
            generateId(), prefillName != null ? prefillName : "", "", 0.0, 0, "", "", ""
        );
    }

    private String generateId() {
        return "P-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private boolean isCancelSignal(String input) {
        return input != null && input.equalsIgnoreCase("cancel");
    }
}
