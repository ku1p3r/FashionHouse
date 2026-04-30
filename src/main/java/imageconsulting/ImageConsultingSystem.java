package imageconsulting;

import catalog.service.CatalogService;
import common.model.Product;

import java.nio.file.Path;
import java.util.*;

public class ImageConsultingSystem {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        CatalogService catalog = new CatalogService(Path.of("stores/fashionstore1.catalog"));
        ProgressTracker tracker = new ProgressTracker();
        List<Client> clients = ClientFileManager.loadClients(catalog);
        List<Consultant> consultants = ConsultantFileManager.loadConsultants();
        for (Client c : clients) {
            for (Consultant con : consultants) {
                if (c.getConsultant() != null && c.getConsultant().equals(con.getName()) && !c.getObservers().contains(con)) {
                    c.addObserver(con);
                }
            }
            c.addObserver(tracker);
        }
        List<ConsultationMeeting> meetings = ConsultationMeetingFileManager.loadMeetings();


        boolean running = true;

        while (running) {
            printMenu();
            int choice = getInt();

            switch (choice) {
                case 1 -> manageConsultants(consultants);
                case 2 -> manageClients(clients, consultants, tracker);
                case 3 -> assignClothing(clients, catalog);
                case 4 -> scheduleConsultation(clients, consultants, meetings);
                case 5 -> trackProgress(clients);
                case 6 -> viewClients(clients);
                case 7 -> viewConsultants(consultants);
                case 8 -> {
                    ClientFileManager.saveClients(clients);
                    ConsultantFileManager.saveConsultants(consultants);
                    ConsultationMeetingFileManager.saveMeetings(meetings);
                    System.out.println("Saved. Exiting...");
                    running = false;
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== IMAGE CONSULTING SYSTEM ===");
        System.out.println("1. Manage Consultants");
        System.out.println("2. Manage Clients");
        System.out.println("3. Assign Clothing");
        System.out.println("4. Schedule Consultation");
        System.out.println("5. Track Progress");
        System.out.println("6. View Clients");
        System.out.println("7. View Consultants");
        System.out.println("8. Save & Exit");
    }

    private static int getInt() {
        while (!scanner.hasNextInt()) {
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void manageConsultants(List<Consultant> consultants) {
        scanner.nextLine();
        System.out.print("Enter consultant name: ");
        String name = scanner.nextLine();

        consultants.add(new Consultant(new Random().nextLong(), name));
        System.out.println("Consultant added.");
    }

    private static void manageClients(List<Client> clients,
                                  List<Consultant> consultants,
                                  ProgressTracker tracker) {

    scanner.nextLine();

    System.out.print("Enter client name: ");
    String name = scanner.nextLine();

    String id = UUID.randomUUID().toString();
    Client client = new Client(id, name);

    client.addObserver(tracker);

    if (consultants.isEmpty()) {
        System.out.println("No consultants available. Please add one first.");
        return;
    }

    System.out.println("\nAvailable Consultants:");
    for (int i = 0; i < consultants.size(); i++) {
        Consultant c = consultants.get(i);
        System.out.println(i + ": " + c.getName() + " | ID: " + c.getId());
    }

    System.out.print("Select consultant index: ");
    int consultantIndex = getInt();
    scanner.nextLine();

    if (consultantIndex < 0 || consultantIndex >= consultants.size()) {
        System.out.println("Invalid selection.");
        return;
    }

    Consultant selectedConsultant = consultants.get(consultantIndex);

    client.setConsultant(selectedConsultant.getName());

    client.addObserver(selectedConsultant);

    System.out.println("Assigned consultant: " + selectedConsultant.getName());

    System.out.print("Enter client goal (what they want from the rebrand): ");
    String goals = scanner.nextLine();
    client.setGoals(goals);

    System.out.print("Enter initial status (e.g., New, In Progress): ");
    String status = scanner.nextLine();
    client.setStatus(status);

    clients.add(client);
    System.out.println("Client created successfully.");
}

    private static void assignClothing(List<Client> clients,
                                       CatalogService catalog) {

        if (clients.isEmpty()) return;

        Client client = selectClient(clients);
        if (client == null) return;

        scanner.nextLine();
        System.out.print("Search catalog: ");
        String query = scanner.nextLine();

        List<Product> results = catalog.search(query);

        for (int i = 0; i < results.size(); i++) {
            System.out.println(i + ": " + results.get(i).getName());
        }

        int choice = getInt();
        Product selected = results.get(choice);

        client.assignClothing(selected);
    }

    private static void scheduleConsultation(List<Client> clients,
                                         List<Consultant> consultants,
                                         List<ConsultationMeeting> meetings) {

        Client client = selectClient(clients);
        if (client == null) return;

        System.out.println("\n1. Add Meeting");
        System.out.println("2. View Meetings");
        System.out.println("3. Delete Meeting");
        int choice = getInt();
        scanner.nextLine();

        switch (choice) {

            case 1 -> {
                System.out.print("Enter date/time (YYYY-MM-DD HH:MM): ");
                String dateTime = scanner.nextLine();

                System.out.print("Enter description: ");
                String desc = scanner.nextLine();

                ConsultationMeeting meeting = new ConsultationMeeting(
                    dateTime,
                    client.getId(),
                    client.getConsultant(),
                    desc
                );

                meetings.add(meeting);
                System.out.println("Meeting scheduled.");
            }

            case 2 -> {
                for (int i = 0; i < meetings.size(); i++) {
                    ConsultationMeeting m = meetings.get(i);

                    if (m.getClientId().equals(client.getId())) {
                        System.out.println(i + ": " +
                            m.getDateTime() + " | " +
                            m.getDescription());
                    }
                }
            }

            case 3 -> {
                List<Integer> validIndexes = new ArrayList<>();

                for (int i = 0; i < meetings.size(); i++) {
                    if (meetings.get(i).getClientId().equals(client.getId())) {
                        validIndexes.add(i);
                        System.out.println(i + ": " +
                            meetings.get(i).getDateTime());
                    }
                }

                if (validIndexes.isEmpty()) {
                    System.out.println("No meetings found.");
                    return;
                }

                System.out.print("Select meeting to delete: ");
                int index = getInt();

                if (!validIndexes.contains(index)) {
                    System.out.println("Invalid selection.");
                    return;
                }

                meetings.remove(index);
                System.out.println("Meeting removed.");

                scanner.nextLine();
                System.out.print("Update client status: ");
                String status = scanner.nextLine();

                client.updateStatus(status);
            }
        }
    }

    private static void trackProgress(List<Client> clients) {

        if (clients.isEmpty()) return;

        Client client = selectClient(clients);
        if (client == null) return;

        System.out.println("1. Update Status");
        System.out.println("2. Close Case");

        int choice = getInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter status: ");
                client.updateStatus(scanner.nextLine());
            }
            case 2 -> client.closeCase();
        }
    }


    private static void viewClients(List<Client> clients) {

        if (clients.isEmpty()) {
            System.out.println("No clients available.");
            return;
        }

        System.out.println("\n=== CLIENTS ===");

        for (int i = 0; i < clients.size(); i++) {
            Client c = clients.get(i);

            System.out.println(i + ": " + c.getName()
                + " | Status: " + (c.getStatus() != null ? c.getStatus() : "N/A")
                + " | Consultant: " + c.getConsultant()
                + " | Open: " + c.isCaseOpen());
        }  

        System.out.print("\nView full details? (y/n): ");
        scanner.nextLine();
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("y")) {

            System.out.print("Enter client index: ");
            int index = getInt();
            scanner.nextLine();

            if (index < 0 || index >= clients.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            Client c = clients.get(index);

            System.out.println("\n--- CLIENT DETAILS ---");
            System.out.println("Name: " + c.getName());
            System.out.println("ID: " + c.getId());
            System.out.println("Consultant: " + c.getConsultant());
            System.out.println("Status: " + c.getStatus());
            System.out.println("Case Open: " + c.isCaseOpen());
            System.out.println("Goals: " + c.getGoals());

            System.out.println("\nWardrobe:");
            if (c.getWardrobeIds().isEmpty()) {
                System.out.println("None assigned.");
            } else {
                for (String id : c.getWardrobeIds()) {
                System.out.println("- " + id);
                }
            }
        }
    }

    private static void viewConsultants(List<Consultant> consultants) {

        if (consultants.isEmpty()) {
            System.out.println("No consultants available.");
            return;
        }

        System.out.println("\n=== CONSULTANTS ===");

        for (int i = 0; i < consultants.size(); i++) {
            Consultant c = consultants.get(i);

            System.out.println(i + ": " +
                c.getName() +
                " | ID: " + c.getId() +
                " | Dept: " + c.getDept() +
                " | Salary: " + c.getSalary());
        }

        System.out.print("\nView full details? (y/n): ");
        scanner.nextLine();
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("y")) {

            System.out.print("Enter consultant index: ");
            int index = getInt();
            scanner.nextLine();

            if (index < 0 || index >= consultants.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            System.out.println("\n--- CONSULTANT DETAILS ---");
            System.out.println(consultants.get(index).toString());
        }
    }

    private static Client selectClient(List<Client> clients) {

        if (clients.isEmpty()) {
            System.out.println("No clients available.");
            return null;
        }

        System.out.println("\nAvailable Clients:");
        for (int i = 0; i < clients.size(); i++) {
            Client c = clients.get(i);
            System.out.println(i + ": " + c.getName() +
                " | Status: " + c.getStatus());
        }

        System.out.print("Select client index: ");
        int index = getInt();
        scanner.nextLine();

        if (index < 0 || index >= clients.size()) {
            System.out.println("Invalid selection.");
            return null;
        }

        return clients.get(index);
    }
}