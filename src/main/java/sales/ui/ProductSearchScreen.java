package sales.ui;

import common.base.Screen;
import sales.ScreenInput;
import sales.service.SalesService;

import java.util.Scanner;

public class ProductSearchScreen  implements Screen {

    private SalesService service;
    private boolean productSelected;
    private String selection;

    public ProductSearchScreen(SalesService service){
        this.productSelected = false;
        this.selection = "";
        this.service = service;
    }

    @Override
    public void show() {
        if(!productSelected){
            System.out.println("Product Search\n");
            service.printAvailableProducts();
            System.out.print("\nInput the Product_ID, or 0 to exit ---> ");
        } else {
            System.out.println("You Selected:");
            System.out.println(selection);
            System.out.println("""
                    
                    1: confirm
                    2: cancel
                    """);
            System.out.print("Your selection ---> ");
        }
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        if(!productSelected){
            String choice = scn.nextLine().trim();

            if(choice.equals("0")){
                return ScreenInput.TO_CART;
            } else {
                selection = choice;
                productSelected = true;
                return ScreenInput.NONE;
            }
        } else {
            int choice = scn.nextInt();

            if(choice == 1){
                service.addToCart(selection);
                productSelected = false;
                return ScreenInput.NONE;
            } else if(choice == 2){
                productSelected = false;
                return ScreenInput.NONE;
            } else {
                return ScreenInput.NONE;
            }
        }
    }

}
