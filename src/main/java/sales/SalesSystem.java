package sales;


import sales.service.SalesService;
import sales.ui.*;

/**
 * @author Mason Hart
 */
public class SalesSystem {



    private static SalesService service = new SalesService();

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
                case EXIT:
                    System.exit(0);
                    break;
                case NEXT_SCREEN:
                    currScreen = currScreen.next();
            }

        } while(input != ScreenInput.EXIT);

    }
}