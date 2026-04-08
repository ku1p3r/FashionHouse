
import common.base.Selectable;
import common.util.Terminal;
import common.wrapper.Option;

import java.util.List;
import java.util.Scanner;


public class Main{

    /**
     * Primary main call for complete program.
     *
     * You can call this again if you want the subprogram to exit to the main screen.
     *
     * @param args
     */
    public static void main(String[] args){
        // Catalog
        Runnable rc = () -> catalog.Main.main(args);
        Option catalog = new Option("1", "Product Catalog and Inventory", rc);

        // Analytics
        Runnable ra = () -> analytics.Main.main(args);
        Option analytics = new Option("2", "DBMS and Analytics", ra);

        // TODO add other services once their Main file is added

        Option exit = new Option("quit", "Exit program", () -> shutdown());

        Terminal.prompt("Select Program", List.of(), List.of(
                catalog,
                analytics,
                /* TODO add other programs */
                exit
        ));
    }

    public static void shutdown(){
        System.out.println(Terminal.YELLOW + "Shutting down." + Terminal.RESET);
    }
}