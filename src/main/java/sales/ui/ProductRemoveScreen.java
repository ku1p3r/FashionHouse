package sales.ui;

import sales.ScreenInput;

import java.util.Scanner;

public class ProductRemoveScreen  implements Screen {
    @Override
    public void show() {
        System.out.print("""
                Remove a product
                
                0: return to cart
                [1-x]: remove
                
                select an option ---> """);
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();

        if(choice == 0){
            return ScreenInput.TO_CART;
        } else {
            return ScreenInput.NONE;
        }
    }

    @Override
    public Screen next() {
        return null;
    }
}
