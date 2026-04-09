package common.model;

import common.base.Selectable;

public class Retailer implements Selectable {
    // TODO add other attributes
    private static int nextId = 0;
    private String name;
    private String id;

    public Retailer(String name){
        this.name = name;
        nextId++;
        this.id = Integer.toString(nextId);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName(){
        return name;
    }
}
