package catalog.ui;

import catalog.service.CatalogService;
import common.base.iScreen;
import common.model.Product;
import common.util.MenuInvoker;
import common.util.Terminal;
import java.io.IOException;
import java.util.List;

/**
 * Main search screen with a search bar and results list.
 * Also handles the "no results" flow and the "create new product" flow.
 */
public class SearchScreen implements iScreen {

    private CatalogService service;
    private ProductForm    form;
    private DetailScreen   detail;

    public SearchScreen(CatalogService service) {
        this.service = service;
        this.form    = new ProductForm(service);
        this.detail  = new DetailScreen(service);
    }

    /** Entry point — loops until the user quits. */
    @Override
    public void run() {
        String query = "";
        while (true) {
            List<Product> results = service.search(query);
            query = handleResults(results, query);
            if (query == null) return;
            if (query.isBlank()) continue;
        }
    }

    private String handleResults(List<Product> results, String query) {
        while (true) {
            Terminal.clearScreen();
            Terminal.printHeader("Merchandise Catalog  —  " + service.getFilePath().getFileName());
            Terminal.println(Terminal.DIM + "Query: \"" + query + "\"  —  " + results.size() + " result(s)" + Terminal.RESET);
            Terminal.println();
            Terminal.printTableHeader("ID", "Name", "Price", "Quantity");

            for (int i = 0; i < results.size(); i++) {
                Product p = results.get(i);
                boolean low = p.getQuantity() < 5;
                String idVal = p.getId();
                String nameVal = p.getName();
                String priceVal = String.format("$%.2f", p.getPrice());
                String qtyVal = String.valueOf(p.getQuantity());
                if (low) qtyVal = qtyVal + " (low on stock)";

                Terminal.printTableRowColored(low ? Terminal.RED : Terminal.RESET, i + 1,
                    idVal,
                    nameVal,
                    priceVal,
                    qtyVal
                );
            }

            Terminal.println();
            Terminal.printLine();
            Terminal.printMenuOption("(1-" + results.size() + ")", "Enter a result number to view details");
            Terminal.printMenuOption("(ID/Keyword)", "Filter on keyword (press enter to remove filter)");
            Terminal.printMenuOption("new", "Create a new product");
            Terminal.printMenuOption("back",   "Select different store");
            Terminal.println();

            String input = Terminal.prompt("Choice >");

            // Named commands handled via MenuInvoker; numeric/keyword handled below
            MenuInvoker nav = new MenuInvoker();
            nav.register("new", "Create a new product", () -> { createNew(null); })
               .register("back", "Select different store", () -> {
                   StoreSelectionScreen storeScreen = new StoreSelectionScreen();
                   CatalogService newService = storeScreen.run();
                   if (newService != null) {
                       this.service = newService;
                       this.form   = new ProductForm(service);
                       this.detail = new DetailScreen(service);
                   }
               });

            if (nav.isRegistered(input)) {
                nav.execute(input);
                if (input.equalsIgnoreCase("back")) return null;
                if (input.equalsIgnoreCase("new"))  return "";
                continue;
            }

            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < results.size()) {
                    Product selected = results.get(index);
                    Product fresh = service.findById(selected.getId()).orElse(selected);
                    DetailScreen.Result result = detail.show(fresh);
                    if (result == DetailScreen.Result.DELETED || result == DetailScreen.Result.UPDATED) {
                        results = service.search(query);
                        if (results.isEmpty()) return "";
                    }
                } else {
                    Terminal.printError("Invalid selection.");
                    Terminal.pressEnterToContinue();
                }
            } catch (NumberFormatException e) {
                return input;
            }
        }
    }

    private void handleNoResults(String query) {
        Terminal.clearScreen();
        Terminal.printHeader("No Results");
        Terminal.println();
        Terminal.printWarning("No products matched: \"" + query + "\"");
        Terminal.println();

        MenuInvoker menu = new MenuInvoker();
        menu.register("create", "Create a new product using \"" + query + "\" as the name",
                        () -> createNew(query))
            .register("back",   "Back to search", menu::stop);

        menu.printOptions();
        Terminal.println();
        menu.execute(Terminal.prompt("Choice >"));
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
