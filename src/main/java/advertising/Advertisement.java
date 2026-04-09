package advertising;

public class Advertisement {
    private final int advertisementId;
    private final String title;
    private final String description;
    private final String targetAudience;
    private final int duration;
    private final String platformName;
    private final String promotedItemName;
    private final String promotedItemType;

    public Advertisement(int advertisementId, String title, String description,
                         String targetAudience, int duration,
                         String platformName, String promotedItemName,
                         String promotedItemType) {
        this.advertisementId = advertisementId;
        this.title = title;
        this.description = description;
        this.targetAudience = targetAudience;
        this.duration = duration;
        this.platformName = platformName;
        this.promotedItemName = promotedItemName;
        this.promotedItemType = promotedItemType;
    }

    public int getAdvertisementId() {
        return advertisementId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTargetAudience() {
        return targetAudience;
    }

    public int getDuration() {
        return duration;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getPromotedItemName() {
        return promotedItemName;
    }

    public String getPromotedItemType() {
        return promotedItemType;
    }

    @Override
    public String toString() {
        return "Advertisement{id=" + advertisementId
                + ", title='" + title + '\''
                + ", description='" + description + '\''
                + ", targetAudience='" + targetAudience + '\''
                + ", duration=" + duration
                + ", platform='" + platformName + '\''
                + ", promotedItem='" + promotedItemName + '\''
                + ", promotedItemType='" + promotedItemType + '\''
                + '}';
    }
}
