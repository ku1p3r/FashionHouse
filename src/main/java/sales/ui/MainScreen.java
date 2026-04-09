package sales.ui;

import sales.ScreenInput;

import java.util.Scanner;

public class MainScreen  implements Screen {
    @Override
    public void show() {
        System.out.print("""
                Sales System
                
                1: new sale
                2: exit
                
                select an option ---> """);
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();

        if(choice == 1){
            return ScreenInput.TO_CART;
        } else if(choice == 2){
            return ScreenInput.EXIT;
        } else {
            return ScreenInput.NONE;
        }
    }

    @Override
    public Screen next() {
        return null;
    }
}
