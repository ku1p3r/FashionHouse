package common.model;

import common.base.Selectable;

public class Retailer implements Selectable {
    // TODO add other attributes
    private String name;
    private int id;

    public Retailer(String name, int id){
        this.name = name;
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public void pick() {
        System.out.println("Store picked: " + name);
    }
}
