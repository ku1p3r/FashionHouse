package sales.service;

import sales.model.Sale;
import sales.model.Receipt;
import common.util.Serializer;

public class SalesService{

    private static final String path = "res/sales.csv";

    private Serializer dbms;

    public SalesService(){
        dbms = new Serializer(path);
    }

    public void sayHi(){
        System.out.println("WIP");
    }

    public void saveSale(Sale s){

    }

    public void getSale(int id){

    }

    public void delSale(int id){

    }

    public void registerReturn(Receipt r){
        
    }
}