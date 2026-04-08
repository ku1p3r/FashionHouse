package common.example;

import common.model.Retailer;
import common.util.Terminal;

import java.util.List;

public class Main {
    public static void main(String args[]){
        Retailer store1 = new Retailer("store 1", 1);
        Retailer store2 = new Retailer("store 2", 2);
        //Option exit = new Option("exit", "Quits program.", null);

        boolean flag = true;
        while(flag) {
            flag = Terminal.prompt("Example", List.of(store1, store2), List.of());
        }
        //Prompter.prompt(List.of(store1, store2), List.of(exit));
    }
}
