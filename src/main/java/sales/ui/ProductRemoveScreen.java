package sales.ui;

import sales.ScreenInput;
import sales.service.SalesService;

import java.util.Scanner;

public class ProductRemoveScreen  implements Screen {

    private SalesService service;
    private boolean productSelected;
    private int selected;

    public ProductRemoveScreen(SalesService s){
        this.selected = -1;
        this.productSelected = false;
        this.service = s;
    }

    @Override
    public void show() {
        if(!productSelected){
            int numItems = service.numItemsInCart();

            System.out.println("Remove Product From Cart\n");
            if(numItems <= 0){
                System.out.print("No items\n\n0: back to cart\n\nYour selection ---> ");
            } else {
                service.printCart();
                System.out.printf("\n0: return to cart\n[1-%d]: select item\n\nYour selection ---> ", service.numItemsInCart());
            }
        } else {
            System.out.println("You selected the following product to remove:\n");
            System.out.println(service.getCartProduct(selected-1));
            System.out.print("\n\n1: confirm\n2: cancel\n\nYour selection ---> ");
        }
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();

        if(!productSelected){
            if(choice == 0){
                return ScreenInput.TO_CART;
            } else if(choice >= 1 && choice <= service.numItemsInCart()){
                selected = choice;
                productSelected = true;
                return ScreenInput.NONE;
            } else {
                return ScreenInput.NONE;
            }
        } else {
            if(choice == 1){
                service.removeFromCart(selected-1);
                productSelected = false;
                return ScreenInput.NONE;
            } else if(choice == 2) {
                productSelected = false;
                return ScreenInput.NONE;
            } else {
                return ScreenInput.NONE;
            }
        }
    }

}
