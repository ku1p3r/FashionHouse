package sales.ui;

import sales.ScreenInput;

import java.util.Scanner;

public class CheckoutScreen  implements Screen {
    @Override
    public void show() {
        System.out.print("""
                Checkout
                
                1: confirm
                2: return to cart
                
                select an option ---> """);
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();

        if(choice == 1){
            // TODO tell the service to save to csv
            return ScreenInput.TO_MAIN;
        } else if(choice == 2){
            return ScreenInput.TO_CART;
        } else {
            return ScreenInput.EXIT;
        }
    }

    @Override
    public Screen next() {
        return null;
    }

}
