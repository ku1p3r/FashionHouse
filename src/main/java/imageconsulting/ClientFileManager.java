package imageconsulting;

import common.model.Product;
import catalog.service.CatalogService;

import java.io.*;
import java.util.*;

public class ClientFileManager {

    private static final String FILE_NAME = "data/imageconsulting/clients.txt";

    public static List<Client> loadClients(CatalogService catalog) {
        List<Client> clients = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1); // keep empty fields

                if (parts.length < 6) continue;

                String id = parts[0];
                String name = parts[1];
                String consultant = parts[2];
                String status = parts[3];
                boolean caseOpen = Boolean.parseBoolean(parts[4]);
                String goals = parts[5];

                Client c = new Client(id, name);
                c.setConsultant(consultant);
                c.setStatus(status);
                c.setCaseOpen(caseOpen);
                c.setGoals(goals);

                // Wardrobe
                if (parts.length >= 7 && !parts[6].isEmpty()) {
                    String[] wardrobeIds = parts[6].split("\\|");

                    List<Product> wardrobe = new ArrayList<>();

                    for (String wid : wardrobeIds) {
                        catalog.findById(wid).ifPresent(wardrobe::add);
                    }

                    c.setWardrobe(wardrobe);
                }

                clients.add(c);
            }

        } catch (IOException e) {
            System.out.println("No client file found.");
        }

        return clients;
    }

    public static void saveClients(List<Client> clients) {
        new File("data/imageconsulting").mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Client c : clients) {

                String wardrobeIds = String.join("|", c.getWardrobeIds());

                writer.write(
                        c.getId() + ";" +
                        c.getName() + ";" +
                        c.getConsultant() + ";" +
                        c.getStatus() + ";" +
                        c.isCaseOpen() + ";" +
                        c.getGoals() + ";" +
                        wardrobeIds
                );

                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving clients.");
        }
    }
}
