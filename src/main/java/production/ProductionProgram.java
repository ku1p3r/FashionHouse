package production;

import catalog.service.CatalogService;
import catalog.ui.StoreSelectionScreen;

public class ProductionProgram {
    public static void main(String[] args) {
        StoreSelectionScreen storeScreen = new StoreSelectionScreen();
        CatalogService service = storeScreen.run();
        ProductionSelectionScreen prodScreen = new ProductionSelectionScreen(service);
        prodScreen.run();
    }
}
