
import java.util.Scanner;


public class Main{

    public static void main(String[] args){

        Scanner scn = new Scanner(System.in);

        System.out.println("Welcome to Fashion House System\n\n");
        System.out.println("""
        Please make a selection:
        
        1. Product Catalog and Inventory
        2. Advertising and Fashion Shows
        3. Legal/IP Manager
        4. DBMS and Analytics
        5. Sales
        
        """);
        System.out.print("Enter Your Selection ---> ");

        int choice = scn.nextInt();
        
        if(choice < 1 || choice > 5){
            System.out.println("Invalid choice. Terminating...");
            System.exit(1);
        }

        switch(choice){
            case 1:
                catalog.Main.run(args);
                break;
            case 2:

                break;
            case 3:

                break;
            case 4:
                
                break;
            case 5:

                break;
            default:
                break;
        }
    }
}