package sales;

import common.base.Screen;
import common.base.ScreenProgramTemplate;
import sales.service.SalesService;
import sales.ui.*;

/**
 * Sales console flow using {@link ScreenProgramTemplate}.
 */
final class SalesProgramScreen extends ScreenProgramTemplate<Screen, ScreenInput> {

    private final SalesService service = new SalesService();
    private final Screen cartScreen;
    private final Screen checkoutScreen;
    private final Screen mainScreen;
    private final Screen productRemoveScreen;
    private final Screen productSearchScreen;
    private final Screen viewSalesScreen;

    SalesProgramScreen() {
        cartScreen = new CartScreen(service);
        checkoutScreen = new CheckoutScreen(service);
        mainScreen = new MainScreen();
        productRemoveScreen = new ProductRemoveScreen(service);
        productSearchScreen = new ProductSearchScreen(service);
        viewSalesScreen = new ViewSalesScreen(service);
    }

    @Override
    protected Screen initialScreen() {
        return mainScreen;
    }

    @Override
    protected void render(Screen screen) {
        screen.show();
    }

    @Override
    protected ScreenInput readInput(Screen screen) {
        return screen.processInput();
    }

    @Override
    protected Screen nextScreen(Screen current, ScreenInput input) {
        return switch (input) {
            case TO_CART -> cartScreen;
            case TO_CHECKOUT -> checkoutScreen;
            case TO_MAIN -> mainScreen;
            case TO_REMOVE -> productRemoveScreen;
            case TO_SEARCH -> productSearchScreen;
            case TO_SALES -> viewSalesScreen;
            default -> current;
        };
    }

    @Override
    protected boolean shouldExit(ScreenInput input) {
        return input == ScreenInput.EXIT;
    }
}
