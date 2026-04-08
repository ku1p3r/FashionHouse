package catalog;

import catalog.service.CatalogService;
import catalog.ui.SearchScreen;
import catalog.ui.StoreSelectionScreen;
import common.util.Terminal;

public class Main {

    public static void main(String[] args) {
        StoreSelectionScreen storeScreen = new StoreSelectionScreen();
        CatalogService service = storeScreen.run();

        if (service == null) {
            Terminal.clearScreen();
            Terminal.println(Terminal.CYAN + "Goodbye." + Terminal.RESET);
            Terminal.println();
            return;
        }

        SearchScreen searchScreen = new SearchScreen(service);
        searchScreen.run();

        Terminal.clearScreen();
        Terminal.println(Terminal.CYAN + "Session closed. Catalog saved to: "
                + service.getFilePath() + Terminal.RESET);
        Terminal.println();
    }
}
