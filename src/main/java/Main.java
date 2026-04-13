
import analytics.AnalyticsProgram;
import catalog.CatalogProgram;
import common.util.Terminal;
import common.wrapper.Option;
import legal.RegisterLicense;

import java.util.List;


public class Main{

    /**
     * Primary main call for complete program.
     *
     * @param args
     */
    public static void main(String[] args){

        Option catalog = new Option("1", "Product Catalog and Inventory", () -> CatalogProgram.main(args));
        Option analytics = new Option("2", "DBMS and Analytics", () -> AnalyticsProgram.main(args));
        Option legal = new Option("3", "IP and License Registration", () -> RegisterLicense.main(args));
        // TODO add other services once their Main file is added

        boolean[] running = {true};
        Option exit = new Option("quit", "Exit program", () -> running[0] = false);

        while(running[0]){
            Terminal.prompt("Select Program", List.of(), List.of(
                    catalog,
                    analytics,
                    legal,
                    /* TODO add other programs */
                    exit
            ));
        }

        System.out.println(Terminal.YELLOW + "Shutting down." + Terminal.RESET);
    }
}