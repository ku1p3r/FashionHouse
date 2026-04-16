package sales.ui;

import common.base.Screen;
import sales.ScreenInput;
import sales.service.SalesService;

import java.util.Scanner;

public class ViewSalesScreen implements Screen {

    private SalesService service;

    public ViewSalesScreen(SalesService s){
        this.service = s;
    }

    @Override
    public void show() {
        System.out.println("View Past Sales\n");
        service.printSales();
        System.out.print("\nPress 0 to exit ---> ");
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();

        if(choice == 0){
            return ScreenInput.TO_MAIN;
        } else {
            return ScreenInput.NONE;
        }
    }
}
