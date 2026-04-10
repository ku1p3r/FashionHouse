package advertising;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Fashion House Project
 * Advertising Module - Iteration 1
 * Use Case: Register New Advertisement
 *
 * Handles advertisement validation, creation, loading, and file storage.
 * Author: Gilbert
 */
public class AdvertisingSystem {
    private static final String FILE_NAME = "src/main/java/advertisements.txt";

    private List<Product> products;
    private List<Collection> collections;
    private List<Platform> platforms;
    private List<Advertisement> advertisements;
    private int nextAdvertisementId;

    public AdvertisingSystem() {
        products = new ArrayList<>();
        collections = new ArrayList<>();
        platforms = new ArrayList<>();
        advertisements = new ArrayList<>();
        nextAdvertisementId = 1;

        loadAdvertisementsFromFile();
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

        boolean platformExists = checkPlatform(platformName);

        return itemExists && platformExists;
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
        advertisements.add(advertisement);
        saveAdvertisementsToFile();
        return true;
    }

    public void registerAdvertisement(String title,
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
            return;
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
    }

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

    private void saveAdvertisementsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Advertisement ad : advertisements) {
                writer.write(
                        ad.getAdvertisementId() + "|" +
                        ad.getTitle() + "|" +
                        ad.getDescription() + "|" +
                        ad.getTargetAudience() + "|" +
                        ad.getDuration() + "|" +
                        ad.getPlatformName() + "|" +
                        ad.getPromotedItemName() + "|" +
                        ad.getPromotedItemType()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving advertisements to file.");
        }
    }

    private void loadAdvertisementsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int maxId = 0;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length == 8) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String description = parts[2];
                    String targetAudience = parts[3];
                    int duration = Integer.parseInt(parts[4]);
                    String platformName = parts[5];
                    String promotedItemName = parts[6];
                    String promotedItemType = parts[7];

                    Advertisement ad = new Advertisement(
                            id,
                            title,
                            description,
                            targetAudience,
                            duration,
                            platformName,
                            promotedItemName,
                            promotedItemType
                    );

                    advertisements.add(ad);

                    if (id > maxId) {
                        maxId = id;
                    }
                }
            }

            nextAdvertisementId = maxId + 1;

        } catch (IOException e) {
            // File may not exist yet on first run, which is okay.
        } catch (NumberFormatException e) {
            System.out.println("Error reading advertisement data from file.");
        }
    }
}