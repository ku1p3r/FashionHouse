package common.example.prompter;

import common.model.Retailer;
import common.util.Terminal;
import common.wrapper.Option;

import java.util.List;

public class Main {
    public static void main(String args[]){
        Retailer store1 = new Retailer("store 1", 1);
        Retailer store2 = new Retailer("store 2", 2);

        TestClass tc = new TestClass();
        Option test = new Option("test", "Action example.", tc::execute);

        boolean flag = true;
        while(flag) {
            flag = Terminal.prompt("Example", List.of(store1, store2), List.of(test));
        }
    }
}
