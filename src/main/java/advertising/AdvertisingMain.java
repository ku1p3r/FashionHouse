package advertising;

public class AdvertisingMain {
    public static void main(String[] args) {
        AdvertisingSystem system = new AdvertisingSystem();

        system.addProduct(new Product(1, "Leather Jacket"));
        system.addProduct(new Product(2, "Silk Dress"));

        system.addCollection(new Collection(1, "Spring 2026"));
        system.addCollection(new Collection(2, "Luxury Nightwear"));

        system.addPlatform(new Platform("Instagram"));
        system.addPlatform(new Platform("Billboard"));
        system.addPlatform(new Platform("Magazine"));

        MarketingManager manager = new MarketingManager(1, "Gilbert");

        manager.registerAdvertisement(
                system,
                "Spring Campaign",
                "Luxury spring collection launch",
                "Young adults",
                30,
                "Spring 2026",
                "Collection",
                "Instagram"
        );
    }
}
