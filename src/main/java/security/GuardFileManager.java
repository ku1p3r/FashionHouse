package security;

import java.io.*;
import java.util.*;

public class GuardFileManager {

    private static final String FILE_NAME = "data/security/guards.txt";

    // LOAD guards from file
    public static List<SecurityGuard> loadGuards() {
        List<SecurityGuard> guards = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                long id = Long.parseLong(parts[0]);
                String name = parts[1];
                int prestige = Integer.parseInt(parts[2]);

                Set<String> skills = new HashSet<>(Arrays.asList(parts[3].split("\\|")));

                boolean available = Boolean.parseBoolean(parts[4]);

                SecurityGuard guard = new SecurityGuard(id, name, prestige, skills);

                if (!available) {
                    guard.assign(); // mark unavailable
                }

                guards.add(guard);
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return guards;
    }

    public static void saveGuards(List<SecurityGuard> guards) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {

        for (SecurityGuard g : guards) {
            String skills = String.join("|", g.getSkills());

            String line = String.join(",",
                    g.getId(),
                    g.getName(),
                    String.valueOf(g.getPrestigeLevel()),
                    skills,
                    String.valueOf(g.isAvailable())
            );

            writer.write(line);
            writer.newLine();
        }

    } catch (IOException e) {
        System.out.println("Error writing file: " + e.getMessage());
    }
    }
}
