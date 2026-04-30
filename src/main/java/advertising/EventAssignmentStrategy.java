package advertising;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Concrete Strategy.
 * This class handles assigning a fashion model to an event.
 */
public class EventAssignmentStrategy implements AssignmentStrategy {

    private static final String MODEL_EVENT_ASSIGNMENTS_FILE = "model_event_assignments.txt";

   
    @Override
    public void assign(FashionModel model, String eventId) {

// duplicate check
try (java.io.BufferedReader reader = new java.io.BufferedReader(
        new java.io.FileReader(MODEL_EVENT_ASSIGNMENTS_FILE))) {

    String line;
    while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\|");

        if (parts.length == 2 &&
            parts[0].equals(model.getId()) &&
            parts[1].equals(eventId)) {

            System.out.println("This model is already assigned to this advertisement.");
            return;
        }
    }

} catch (java.io.IOException e) {}

        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MODEL_EVENT_ASSIGNMENTS_FILE, true))) {
            writer.write(model.getId() + "|" + eventId);
            writer.newLine();

            System.out.println("Model successfully assigned to event.");
        } catch (IOException e) {
            System.out.println("Error saving model-event assignment: " + e.getMessage());
        }
    }





}