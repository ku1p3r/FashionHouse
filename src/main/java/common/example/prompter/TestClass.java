package common.example.prompter;

import common.base.Selectable;

public class TestClass {
    public void pick(Selectable s){
        System.out.println("Picked " + s.getName());
    }
    public void execute(){
        System.out.println("Test class executed.");
    }
}
