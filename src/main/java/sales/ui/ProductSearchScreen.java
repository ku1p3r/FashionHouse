package sales.ui;

import sales.ScreenInput;

import java.util.Scanner;

public class ProductSearchScreen  implements Screen {
    @Override
    public void show() {
        System.out.print("""
                Product Search
                
                Input the Product_ID, or 0 to exit ---> """);
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
