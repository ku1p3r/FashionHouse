package imageconsulting;

import java.io.*;
import java.util.*;

public class ConsultantFileManager {

    private static final String FILE_NAME = "data/imageconsulting/consultants.txt";

    public static List<Consultant> loadConsultants() {
        List<Consultant> consultants = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length < 3) continue;

                long id = Long.parseLong(parts[0]);
                String name = parts[1];

                Consultant c = new Consultant(id, name);

                consultants.add(c);
            }

        } catch (IOException e) {
            System.out.println("No consultants file found.");
        }

        return consultants;
    }

    public static void saveConsultants(List<Consultant> consultants) {
        new File("data/imageconsulting").mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Consultant c : consultants) {
                writer.write(
                        c.getId() + ";" +
                        c.getName() + ";" +
                        c.getSalary() + ";" +
                        c.getStartDate() + ";" +
                        c.isActive()
                    );
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving consultants.");
        }
    }
}