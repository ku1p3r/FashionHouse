package sales.ui;

import sales.SalesSystem;
import sales.ScreenInput;
import sales.service.SalesService;
import sales.service.Service;

import java.util.Scanner;

public class CartScreen implements Screen {

    private Service service;

    public CartScreen(Service service){
        this.service = service;
    }

    public void show(){
        System.out.print("""
                Your Cart
                
                1: add items
                2: remove items
                3: cancel
                
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
            return ScreenInput.TO_MAIN;
        } else {
            return ScreenInput.NONE;
        }
    }

    @Override
    public Screen next() {
        return null;
    }

}
