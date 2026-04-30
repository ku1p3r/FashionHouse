package advertising;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Concrete Strategy.
 * This class handles assigning a fashion model to an advertisement.
 */
public class AdvertisementAssignmentStrategy implements AssignmentStrategy {

    private static final String MODEL_AD_ASSIGNMENTS_FILE = "model_ad_assignments.txt";

    
    @Override
    public void assign(FashionModel model, String advertisementId) {



try (java.io.BufferedReader reader = new java.io.BufferedReader(
            new java.io.FileReader(MODEL_AD_ASSIGNMENTS_FILE))) {

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");

            if (parts.length == 2 &&
                parts[0].equals(model.getId()) &&
                parts[1].equals(advertisementId)) {

                System.out.println("This model is already assigned to this advertisement.");
                return;
            }
        }

    } catch (java.io.IOException e) {
        // File may not exist yet, so ignore
    }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MODEL_AD_ASSIGNMENTS_FILE, true))) {
            writer.write(model.getId() + "|" + advertisementId);
            writer.newLine();

            System.out.println("Model successfully assigned to advertisement.");
        } catch (IOException e) {
            System.out.println("Error saving model-advertisement assignment: " + e.getMessage());
        }
    }
}