package sales.ui;

import common.base.Screen;
import sales.ScreenInput;
import sales.service.SalesService;

import java.util.Scanner;

public class CheckoutScreen  implements Screen {

    private SalesService service;
    private boolean saleConfirmed;

    public CheckoutScreen(SalesService s){
        this.saleConfirmed = false;
        this.service = s;
    }

    @Override
    public void show() {
        System.out.println("Checkout ($"+service.getTotal().toString()+")\n\n");
        service.printCart();
        System.out.println("\n\nEnter payment info, or 0 to exit\n\n");
        System.out.print("Your selection ---> ");
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        String choice = scn.nextLine().trim();

        if(choice.equals("0")){
            return ScreenInput.TO_CART;
        } else {
            service.saveSale();
            service.emptyCart();
            return ScreenInput.TO_MAIN;
        }
    }

}
