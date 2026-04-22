package sales;

import common.base.Screen;
import sales.service.SalesService;
import sales.ui.*;
import java.util.Map;


/**
 * @author Mason Hart
 */
public class SalesSystem {


    private static SalesService service = new SalesService();

    static Screen cartScreen = new CartScreen(service);
    static Screen checkoutScreen = new CheckoutScreen(service);
    static Screen mainScreen = new MainScreen();
    static Screen productRemoveScreen = new ProductRemoveScreen(service);
    static Screen productSearchScreen = new ProductSearchScreen(service);
    static Screen viewSalesScreen = new ViewSalesScreen(service);

    /** Maps each navigation signal to the Screen it should activate. */
    private static final Map<ScreenInput, Screen> SCREEN_MAP = Map.of(
            ScreenInput.TO_CART,     cartScreen,
            ScreenInput.TO_CHECKOUT, checkoutScreen,
            ScreenInput.TO_MAIN,     mainScreen,
            ScreenInput.TO_REMOVE,   productRemoveScreen,
            ScreenInput.TO_SEARCH,   productSearchScreen,
            ScreenInput.TO_SALES,    viewSalesScreen
    );

    static Screen currScreen;

    public static void main(String[] args){

        currScreen = mainScreen;

        // main loop
        ScreenInput input;
        do {

            currScreen.show();
            input = currScreen.processInput();
            // Dispatch: look up next screen; NONE and EXIT leave currScreen unchanged
            currScreen = SCREEN_MAP.getOrDefault(input, currScreen);
        } while (input != ScreenInput.EXIT);

    }

}