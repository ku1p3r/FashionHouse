package analytics.services;

import common.model.Product;
import common.model.Retailer;
import common.util.Serializer;
import common.util.Terminal;

import java.nio.file.Path;
import java.util.HashMap;

public class RetailerAnalyzer {
    private static final Path BASE_DIRECTORY = Path.of("retailers");
    private HashMap<Product, Integer> inventory = new HashMap<>();
    private Retailer retailer;

    public RetailerAnalyzer(Retailer retailer){
        this.retailer = retailer;
    }

    public void stock(Product product, int quantity){
        if(inventory.containsKey(product)){
            inventory.replace(product, inventory.get(product) + quantity);
        }else{
            inventory.put(product, quantity);
        }
    }

    public void save() throws Exception {
        Serializer serializer = new Serializer(new String[]{"month","product","initial","stocked","sold"});

        String filePath = BASE_DIRECTORY.resolve(retailer.getName() + ".retailer").toString();
        System.out.println(Terminal.GREEN + "Saved " + filePath + Terminal.RESET);

        serializer.save(filePath);
    }

    public Retailer getRetailer(){
        return retailer;
    }
}
