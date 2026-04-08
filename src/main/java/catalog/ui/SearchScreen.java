package catalog.ui;

import common.model.Product;
import catalog.service.CatalogService;
import common.util.Terminal;

import java.io.IOException;
import java.util.List;

/**
 * Main search screen with a search bar and results list.
 * Also handles the "no results" flow and the "create new product" flow.
 */
public class SearchScreen {

    private CatalogService service;
    private final ProductForm    form;
    private final DetailScreen   detail;

    public SearchScreen(CatalogService service) {
        this.service = service;
        this.form    = new ProductForm(service);
        this.detail  = new DetailScreen(service);
    }

    /** Entry point — loops until the user quits. */
    public void run() {
        while (true) {
            String query = showSearchBar();
            if (query == null) return;
            if (query.isBlank()) continue;

            if (query.equalsIgnoreCase("new")) {
                createNew(null);
                continue;
            }

            List<Product> results = service.search(query);

            if (results.isEmpty()) {
                handleNoResults(query);
            } else {
                handleResults(results, query);
            }
        }
    }

    private String showSearchBar() {
        Terminal.clearScreen();
        Terminal.printHeader("Merchandise Catalog  —  " + service.getFilePath().getFileName());
        Terminal.println(Terminal.DIM + "Products in catalog: " + service.size() + Terminal.RESET);
        Terminal.println();
        Terminal.printMenuOption("new",  "Create a new product");
        Terminal.printMenuOption("store", "Change store");
        Terminal.printMenuOption("quit", "Exit the application");
        Terminal.println();
        Terminal.printLine();
        String query = Terminal.prompt("Search (ID / keyword) >");
        if (query.equalsIgnoreCase("quit") || query.equalsIgnoreCase("q")) return null;
        if (query.equalsIgnoreCase("store")) {
            StoreSelectionScreen storeScreen = new StoreSelectionScreen();
            CatalogService newService = storeScreen.run();
            if (newService != null) {
                this.service = newService;
            }
            return "";
        }
        return query;
    }

    private void handleResults(List<Product> results, String query) {
        while (true) {
            Terminal.clearScreen();
            Terminal.printHeader("Search Results");
            Terminal.println(Terminal.DIM + "Query: \"" + query + "\"  —  " + results.size() + " result(s)" + Terminal.RESET);
            Terminal.println();
            Terminal.printTableHeader("ID", "Name", "Category", "Price");

            for (int i = 0; i < results.size(); i++) {
                Product p = results.get(i);
                Terminal.printTableRow(
                    i + 1,
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    String.format("$%.2f", p.getPrice())
                );
            }

            Terminal.println();
            Terminal.printLine();
            Terminal.printMenuOption("(1-" + results.size() + ")", "Enter a result number to view details");
            Terminal.printMenuOption("new", "Create a new product");
            Terminal.printMenuOption("back",   "Back to search");
            Terminal.println();

            String input = Terminal.prompt("Choice >");

            if (input.equalsIgnoreCase("back") || input.isBlank()) return;
            if (input.equalsIgnoreCase("new")) { createNew(null); return; }

            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < results.size()) {
                    Product selected = results.get(index);
                    Product fresh = service.findById(selected.getId()).orElse(selected);
                    DetailScreen.Result result = detail.show(fresh);
                    if (result == DetailScreen.Result.DELETED || result == DetailScreen.Result.UPDATED) {
                        results = service.search(query);
                        if (results.isEmpty()) return;
                    }
                } else {
                    Terminal.printError("Invalid selection.");
                    Terminal.pressEnterToContinue();
                }
            } catch (NumberFormatException e) {
                Terminal.printError("Please enter a number or a command.");
                Terminal.pressEnterToContinue();
            }
        }
    }

    private void handleNoResults(String query) {
        Terminal.clearScreen();
        Terminal.printHeader("No Results");
        Terminal.println();
        Terminal.printWarning("No products matched: \"" + query + "\"");
        Terminal.println();
        Terminal.printMenuOption("create", "Create a new product using \"" + query + "\" as the name");
        Terminal.printMenuOption("back", "Back to search");
        Terminal.println();

        String choice = Terminal.prompt("Choice >");
        if (choice.equalsIgnoreCase("create")) {
            createNew(query);
        }
        // 'b' or anything else → back to search (loop in run())
    }

    private void createNew(String prefillName) {
        Product newProduct = form.run(null, ProductForm.Mode.CREATE, prefillName);
        if (newProduct == null) return;

        try {
            service.addProduct(newProduct);
            Terminal.clearScreen();
            Terminal.printSuccess("Product \"" + newProduct.getName() + "\" created successfully.");
            Terminal.pressEnterToContinue();
        } catch (IOException e) {
            Terminal.printError("Failed to save product: " + e.getMessage());
            Terminal.pressEnterToContinue();
        }
    }
}
