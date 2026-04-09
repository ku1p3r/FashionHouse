package common.model;

import common.base.Selectable;

public class Retailer implements Selectable {
    // TODO add other attributes
    private static int nextId = 0;
    private String name;
    private int id;

    public Retailer(String name){
        this.name = name;
        this.id = nextId++;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName(){
        return name;
    }
}
