package security;

import java.io.*;
import java.util.*;

public class CampusFileManager {

    private static final String FILE_NAME = "data/security/campuses.txt";

    public static List<Campus> loadCampuses() {
        List<Campus> campuses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String location = parts[2];
                    int securityLevel = Integer.parseInt(parts[3]);

                    campuses.add(new Campus(id, name, location, securityLevel));
                }
            }

        } catch (IOException e) {
            System.out.println("No campuses file found.");
        }

        return campuses;
    }

    public static void saveCampuses(List<Campus> campuses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Campus c : campuses) {
                writer.write(
                        c.getCampusId() + "|" +
                        c.getName() + "|" +
                        c.getLocation() + "|" +
                        c.getSecurityLevel()
                );
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving campuses.");
        }
    }
}
