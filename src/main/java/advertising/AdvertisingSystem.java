package advertising;
/*
 * Advertising Module - Iteration 1
 * Use Case: Register New Advertisement
 *
 * Handles advertisement validation, creation, and storage.
 * Author: Gilbert
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdvertisingSystem {
    private final List<Product> products;
    private final List<Collection> collections;
    private final List<Platform> platforms;
    private final List<Advertisement> advertisements;
    private int nextAdvertisementId;

    public AdvertisingSystem() {
        products = new ArrayList<>();
        collections = new ArrayList<>();
        platforms = new ArrayList<>();
        advertisements = new ArrayList<>();
        nextAdvertisementId = 1;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void addCollection(Collection collection) {
        collections.add(collection);
    }

    public void addPlatform(Platform platform) {
        platforms.add(platform);
    }

    public void displayAdForm() {
        System.out.println("Displaying advertisement registration form...");
    }

    public boolean validateInformation(String title, String description,
                                       String targetAudience, int duration,
                                       String promotedItemName,
                                       String promotedItemType,
                                       String platformName) {
        if (title == null || title.isBlank()) return false;
        if (description == null || description.isBlank()) return false;
        if (targetAudience == null || targetAudience.isBlank()) return false;
        if (duration <= 0) return false;
        if (promotedItemName == null || promotedItemName.isBlank()) return false;
        if (promotedItemType == null || promotedItemType.isBlank()) return false;
        if (platformName == null || platformName.isBlank()) return false;

        boolean itemExists = false;
        if (promotedItemType.equalsIgnoreCase("Product")) {
            itemExists = checkProduct(promotedItemName);
        } else if (promotedItemType.equalsIgnoreCase("Collection")) {
            itemExists = checkCollection(promotedItemName);
        }

        return itemExists && checkPlatform(platformName);
    }

    public boolean checkProduct(String productName) {
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkCollection(String collectionName) {
        for (Collection collection : collections) {
            if (collection.getName().equalsIgnoreCase(collectionName)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPlatform(String platformName) {
        for (Platform platform : platforms) {
            if (platform.getPlatformName().equalsIgnoreCase(platformName)) {
                return true;
            }
        }
        return false;
    }

    public Advertisement createAdvertisement(String title,
                                             String description,
                                             String targetAudience,
                                             int duration,
                                             String promotedItemName,
                                             String promotedItemType,
                                             String platformName) {
        return new Advertisement(
                nextAdvertisementId++,
                title,
                description,
                targetAudience,
                duration,
                platformName,
                promotedItemName,
                promotedItemType
        );
    }

    public boolean storeAdvertisement(Advertisement advertisement) {
        return advertisements.add(advertisement);
    }

    public boolean registerAdvertisement(String title,
                          String description,
                                         String targetAudience,
                               int duration,
                                         String promotedItemName,
                                  String promotedItemType,
                                         String platformName) {
        displayAdForm();

        if (!validateInformation(title, description, targetAudience, duration,
                promotedItemName, promotedItemType, platformName)) {
            System.out.println("Advertisement registration failed: invalid information.");
            return false;
        }

        Advertisement advertisement = createAdvertisement(
                title, description, targetAudience, duration,
                promotedItemName, promotedItemType, platformName
        );

        boolean saved = storeAdvertisement(advertisement);

        if (saved) {
            System.out.println("Advertisement registered successfully.");
            System.out.println(advertisement);
        } else {
            System.out.println("Advertisement registration failed: could not save advertisement.");
        }

        return saved;
    }

    public List<Advertisement> getAdvertisements() {
        return Collections.unmodifiableList(advertisements);
    }
}
