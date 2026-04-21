
import analytics.AnalyticsProgram;
import catalog.CatalogProgram;
import common.util.Terminal;
import common.wrapper.Option;
import java.util.List;
import production.ProductionProgram;
import sales.SalesSystem;


public class Main{

    /**
     * Primary main call for complete program.
     *
     * @param args
     */
    public static void main(String[] args){

         Option catalog = new Option("1", "Product Catalog and Inventory", () -> CatalogProgram.main(args));
         Option analytics = new Option("2", "DBMS and Analytics", () -> AnalyticsProgram.main(args));
         Option sales = new Option("3", "Sales & Retailer Console", () -> SalesSystem.main(args));
         Option production = new Option("4", "Production Management", () -> ProductionProgram.main(args));
         // TODO add other services once their Main file is added

         boolean[] running = {true};
         Option exit = new Option("quit", "Exit program", () -> running[0] = false);

         while(running[0]){
             Terminal.clearScreen();
             Terminal.printHeader("Fashion House");
             Terminal.prompt("Select Program", List.of(), List.of(
                     catalog,
                     analytics,
                     sales,
                     production,
                     /* TODO add other programs */
                     exit
             ));
         }

         System.out.println(Terminal.YELLOW + "Shutting down." + Terminal.RESET);
    }
}