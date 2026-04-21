package common.util;

import common.model.Product;
import common.model.Retailer;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityLoader {
    private Iterable<Product> products;
    private HashMap<String, Product> nameToProduct;
    private HashMap<String, Product> idToProduct;
    private Iterable<Retailer> retailers;
    private HashMap<String, Retailer> nameToRetailer;
    private HashMap<String, Retailer> idToRetailer;

    public EntityLoader() {
        ArrayList<Product> allProducts = new ArrayList<>();
        nameToProduct = new HashMap<>();
        idToProduct = new HashMap<>();
        ArrayList<Retailer> allRetailers = new ArrayList<>();
        nameToRetailer = new HashMap<>();
        idToRetailer = new HashMap<>();

        File storesDir = new File("stores");
        File[] catalogFiles = storesDir.listFiles((dir, name) -> name.endsWith(".catalog"));

        if(catalogFiles != null){
            for(File file : catalogFiles){
                Serializer serializer = new Serializer(file.getPath());
                String rn = file.getName().substring(0, file.getName().length() - ".catalog".length());
                //System.out.println("Loading retailer: " + rn);
                Retailer retailer = new Retailer(rn);

                for(int i = 0; i < serializer.size(); i++){
                    allProducts.add(new Product(
                            serializer.get("id", i, String.class),
                            serializer.get("name", i, String.class),
                            serializer.get("category", i, String.class),
                            serializer.get("price", i, Double.class),
                            serializer.get("quantity", i, Integer.class),
                            serializer.get("description", i, String.class),
                            serializer.get("supplier", i, String.class),
                            serializer.get("materials", i, String.class)
                    ));

                    nameToProduct.put(serializer.get("name", i, String.class), allProducts.get(allProducts.size() - 1));
                    idToProduct.put(serializer.get("id", i, String.class), allProducts.get(allProducts.size() - 1));
                }

                nameToRetailer.put(retailer.getName(), retailer);
                idToRetailer.put(retailer.getId(), retailer);
                allRetailers.add(retailer);
            }
        }

        products = allProducts;
        retailers = allRetailers;
    }

    public Product getProductByName(String name) {
        return nameToProduct.get(name);
    }

    public Product getProductById(String id) {
        return idToProduct.get(id);
    }

    public Iterable<Product> getAllProducts() {
        return products;
    }

    public Retailer getRetailerByName(String name) {
        return nameToRetailer.get(name);
    }

    public Retailer getRetailerById(String id) {
        return idToRetailer.get(id);
    }

    public Iterable<Retailer> getAllRetailers() {
        return retailers;
    }
}
