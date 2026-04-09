package sales;

import sales.service.SalesService;
import sales.service.Service;
import sales.ui.*;


/**
 * @author Mason Hart
 */
public class SalesSystem {


    private static Service service = new SalesService();

    static Screen cartScreen = new CartScreen(service);
    static Screen checkoutScreen = new CheckoutScreen();
    static Screen mainScreen = new MainScreen();
    static Screen productRemoveScreen = new ProductRemoveScreen();
    static Screen productSearchScreen = new ProductSearchScreen();

    static Screen currScreen;

    public static void main(String[] args){

        currScreen = mainScreen;

        // main loop
        ScreenInput input;
        do {

            currScreen.show();
            input = currScreen.processInput();

            switch(input){
                case TO_CART -> {
                    currScreen = cartScreen;
                }
                case TO_CHECKOUT -> {
                    currScreen = checkoutScreen;
                }
                case TO_MAIN -> {
                    currScreen = mainScreen;
                }
                case TO_REMOVE -> {
                    currScreen = productRemoveScreen;
                }
                case TO_SEARCH -> {
                    currScreen = productSearchScreen;
                }
            }

        } while(input != ScreenInput.EXIT);

    }

}