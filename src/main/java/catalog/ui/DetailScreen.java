package catalog.ui;

import catalog.service.CatalogService;
import common.model.Product;
import common.util.Terminal;
import hr.model.Employee;
import java.io.IOException;
import production.AuthService;
import production.MaterialRepository;
import production.ProductionModule;
import production.ProductionRepository;

/**
 * Full product detail view with edit / delete actions.
 * Returns an action signal so the caller (SearchScreen) knows what happened.
 */
public class DetailScreen {
    private static final String REQUIRED_ROLE = "Production Manager";
    public enum Result { BACK, DELETED, UPDATED }

    private final CatalogService service;
    private final ProductForm form;

    public DetailScreen(CatalogService service) {
        this.service = service;
        this.form = new ProductForm(service);
    }

    public Result show(Product product) {
        return new DetailMenuScreen(this, product).runForResult();
    }

    void renderProductDetail(Product product) {
        Terminal.clearScreen();
        Terminal.printHeader("Product Detail");

        Terminal.printSubHeader("Identity");
        Terminal.printField("ID", product.getId());
        Terminal.printField("Name", product.getName());

        Terminal.printSubHeader("Classification");
        Terminal.printField("Category", product.getCategory());
        Terminal.printField("Supplier", product.getSupplier());

        Terminal.printSubHeader("Pricing & Stock");
        Terminal.printField("Price", String.format("$%.2f", product.getPrice()));
        if (product.getQuantity() < 5) {
            Terminal.printFieldWithError("Quantity", String.valueOf(product.getQuantity()), "(low on stock)");
        } else {
            Terminal.printField("Quantity", String.valueOf(product.getQuantity()));
        }

        Terminal.printSubHeader("Details");
        Terminal.printField("Description", product.getDescription());
        Terminal.printField("Materials", product.getMaterials() == null ? "" : product.getMaterials());

        Terminal.println();
        Terminal.printLine();
        Terminal.printMenuOption("edit", "Edit this product");
        Terminal.printMenuOption("restock", "Restock this product");
        Terminal.printMenuOption("delete", "Delete this product");
        Terminal.printMenuOption("back", "Back to search");
        Terminal.println();
    }

    /**
     * @return null to keep showing the detail menu, or a terminal result for {@link #show(Product)}.
     */
    Result handleDetailMenuChoice(Product product, String choice) {
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
                return null;
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
                return null;
            }
            case "back" -> {
                return Result.BACK;
            }
            case "restock" -> {
                ProductionRepository prodRepo = new ProductionRepository(service);
                MaterialRepository matRepo = new MaterialRepository();
                AuthService auth = new AuthService();
                Employee user = auth.authenticate(REQUIRED_ROLE);
                if (user == null) {
                    Terminal.printError("Authentication failed. Exiting.");
                    return null;
                }
                ProductionModule prodModule = new ProductionModule(prodRepo, matRepo, user);
                prodModule.createBatch(product.getId());
                return null;
            }
            default -> {
                return null;
            }
        }
    }
}
