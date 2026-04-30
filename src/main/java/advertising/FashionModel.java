package advertising;

/**
 * Represents a fashion model in the advertising system.
 * Stores profile information needed before assigning the model
 * to events or advertisements.
 */
public class FashionModel {

    private String id;
    private String name;
    private String agency;
    private String category;     
    private String imagePath;    
    private String status;       

    public FashionModel(String id, String name, String agency, String category, String imagePath, String status) {
        this.id = id;
        this.name = name;
        this.agency = agency;
        this.category = category;
        this.imagePath = imagePath;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAgency() {
        return agency;
    }

    public String getCategory() {
        return category;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getStatus() {
        return status;
    }

    
    public String serialize() {
        return id + "|" + name + "|" + agency + "|" + category + "|" + imagePath + "|" + status;
    }

   
    public static FashionModel deserialize(String line) {
        String[] parts = line.split("\\|");

        return new FashionModel(
            parts[0], // id
            parts[1], // name
            parts[2], // agency
            parts[3], // category
            parts[4], // image path
            parts[5]  // status
        );
    }

   
    public String displayProfile() {
        return
            "----------------------------------------\n" +
            "Model ID   : " + id + "\n" +
            "Name       : " + name + "\n" +
            "Agency     : " + agency + "\n" +
            "Category   : " + category + "\n" +
            "Image Path : " + imagePath + "\n" +
            "Status     : " + status + "\n" +
            "----------------------------------------";
    }
}