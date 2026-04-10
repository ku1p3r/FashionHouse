package advertising;
/*
 * 
 * Advertising Module - Iteration 1
 * Use Case: Register New Advertisement
 *
 * Represents the marketing manager who creates advertisements.
 * Author: Gilbert
 */
public class MarketingManager {
    private final int managerId;
    private final String name;

    public MarketingManager(int managerId, String name) {
        this.managerId = managerId;
        this.name = name;
    }

    public int getManagerId() {
        return managerId;
    }

    public String getName() {
        return name;
    }

    public void registerAdvertisement(AdvertisingSystem system,
                                      String title,
                                      String description,
                                      String targetAudience,
                                      int duration,
                                      String promotedItemName,
                                      String promotedItemType,
                                  String platformName) {
        system.registerAdvertisement(title, description, targetAudience, duration,
                promotedItemName, promotedItemType, platformName);
    }
}
