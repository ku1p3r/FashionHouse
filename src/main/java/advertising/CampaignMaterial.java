package advertising;

public class CampaignMaterial {
    private final int imageId;
    private final String filePath;
    private final String caption;

    public CampaignMaterial(int imageId, String filePath, String caption) {
        this.imageId = imageId;
        this.filePath = filePath;
        this.caption = caption;
    }

    public int getImageId() {
        return imageId;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getCaption() {
        return caption;
    }

    @Override
    public String toString() {
        return "CampaignMaterial{" +
                "imageId=" + imageId +
                ", filePath='" + filePath + '\'' +
                ", caption='" + caption + '\'' +
                '}';
    }
}