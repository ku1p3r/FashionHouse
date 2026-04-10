package sales.ui;

import sales.SalesSystem;
import sales.ScreenInput;
import sales.service.SalesService;
import sales.service.Service;

import java.util.Scanner;

public class CartScreen implements Screen {

    private SalesService service;

    public CartScreen(SalesService service){
        this.service = service;
    }

    public void show(){
        System.out.println("Your Cart\n");
        service.printCart();
        System.out.print("""
                
                1: add items
                2: remove items
                3: go to checkout
                4: cancel
                
                select an option ---> """);
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();

        if(choice == 1){
            return ScreenInput.TO_SEARCH;
        } else if(choice == 2){
            return ScreenInput.TO_REMOVE;
        } else if(choice == 3){
            return ScreenInput.TO_CHECKOUT;
        } else if(choice == 4) {
            return ScreenInput.TO_MAIN;
        } else{
            return ScreenInput.NONE;
        }
    }

}
