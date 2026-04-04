package catalog.ui;

import catalog.model.Product;
import catalog.service.CatalogService;
import java.io.IOException;

/**
 * Full product detail view with edit / delete actions.
 * Returns an action signal so the caller (SearchScreen) knows what happened.
 */
public class DetailScreen {

    public enum Result { BACK, DELETED, UPDATED }

    private final CatalogService service;
    private final ProductForm form;

    public DetailScreen(CatalogService service) {
        this.service = service;
        this.form = new ProductForm(service);
    }

    public Result show(Product product) {
        while (true) {
            Terminal.clearScreen();
            Terminal.printHeader("Product Detail");

            // Identity block
            Terminal.printSubHeader("Identity");
            Terminal.printField("ID",       product.getId());
            Terminal.printField("Name",     product.getName());

            // Classification block
            Terminal.printSubHeader("Classification");
            Terminal.printField("Category", product.getCategory());
            Terminal.printField("Supplier", product.getSupplier());

            // Pricing & Stock block
            Terminal.printSubHeader("Pricing & Stock");
            Terminal.printField("Price",    String.format("$%.2f", product.getPrice()));
            Terminal.printField("Quantity", String.valueOf(product.getQuantity()));

            // Details block
            Terminal.printSubHeader("Details");
            Terminal.printField("Description", product.getDescription());

            // Actions
            Terminal.println();
            Terminal.printLine();
            Terminal.printMenuOption("edit", "Edit this product");
            Terminal.printMenuOption("delete", "Delete this product");
            Terminal.printMenuOption("back", "Back to search");
            Terminal.println();

            String choice = Terminal.prompt("Action >");

            switch (choice.toLowerCase()) {
                case "edit" -> {
                    Product updated = form.run(product, ProductForm.Mode.EDIT, null);
                    if (updated != null) {
                        try {
                            service.updateProduct(updated);
                            Terminal.clearScreen();
                            Terminal.printSuccess("Product updated successfully.");
                            Terminal.pressEnterToContinue();
                            return Result.UPDATED;
                        } catch (IOException e) {
                            Terminal.printError("Failed to save: " + e.getMessage());
                            Terminal.pressEnterToContinue();
                        }
                    }
                }
                case "delete" -> {
                    Terminal.println();
                    Terminal.printWarning("You are about to delete: " + product.getName() + " (" + product.getId() + ")");
                    if (Terminal.confirm("Confirm delete?")) {
                        try {
                            service.deleteProduct(product.getId());
                            Terminal.clearScreen();
                            Terminal.printSuccess("Product deleted.");
                            Terminal.pressEnterToContinue();
                            return Result.DELETED;
                        } catch (IOException e) {
                            Terminal.printError("Failed to delete: " + e.getMessage());
                            Terminal.pressEnterToContinue();
                        }
                    } else {
                        Terminal.printInfo("Deletion cancelled.");
                        Terminal.pressEnterToContinue();
                    }
                }
                case "back" -> { return Result.BACK; }
                default  -> { /* ignore unknown */ }
            }
        }
    }
}
