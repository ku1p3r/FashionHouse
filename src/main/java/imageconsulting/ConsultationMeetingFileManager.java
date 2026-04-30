package imageconsulting;

import java.io.*;
import java.util.*;

public class ConsultationMeetingFileManager {

    private static final String FILE_NAME = "data/imageconsulting/meetings.txt";

    public static List<ConsultationMeeting> loadMeetings() {
        List<ConsultationMeeting> meetings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4); // allow commas in description

                if (parts.length < 4) continue;

                meetings.add(new ConsultationMeeting(
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3]
                ));
            }

        } catch (IOException e) {
            System.out.println("No meetings file found.");
        }

        return meetings;
    }

    public static void saveMeetings(List<ConsultationMeeting> meetings) {
        new File("data/imageconsulting").mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (ConsultationMeeting m : meetings) {
                writer.write(
                        m.getDateTime() + "," +
                        m.getClientId() + "," +
                        m.getConsultantName() + "," +
                        m.getDescription()
                );
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving meetings.");
        }
    }
}