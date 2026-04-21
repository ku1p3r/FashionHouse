package security;

import advertising.AdvertisingSystem;
import advertising.Event;
import security.command.*;
import java.util.*;

public class SecurityProgram {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        List<SecurityGuard> guards = GuardFileManager.loadGuards();
        List<Campus> campuses = CampusFileManager.loadCampuses();

        AdvertisingSystem advertisingSystem = new AdvertisingSystem();
        advertisingSystem.loadEventsFromFile();
        List<Event> events = advertisingSystem.viewEvents();

        SecurityService service = new SecurityService();

        // --- Build CommandInvoker and register all commands ---
        CommandInvoker invoker = new CommandInvoker();

        invoker.register(1, new ViewEventsCommand(events));
        invoker.register(2, new ViewCampusesCommand(campuses));
        invoker.register(3, new ViewGuardsCommand(guards));
        invoker.register(4, new AssignGuardsToEventCommand(events, guards, service, scanner));
        invoker.register(5, new AssignGuardsToCampusCommand(campuses, guards, service, scanner));
        invoker.register(6, new SaveAndExitCommand(guards, campuses, invoker));

        // --- Main loop: dispatch user choice through the invoker ---
        while (invoker.isRunning()) {
            printMainMenu();
            int choice = getIntInput();
            invoker.execute(choice);
        }
    }

    // ================= MENU =================

    private static void printMainMenu() {
        System.out.println("\n=== SECURITY MANAGEMENT SYSTEM ===");
        System.out.println("1. View Events");
        System.out.println("2. View Campuses");
        System.out.println("3. View Guards");
        System.out.println("4. Assign Guards to Event");
        System.out.println("5. Assign Guards to Campus");
        System.out.println("6. Save & Exit");
        System.out.print("Select option: ");
    }

    // ================= HELPERS =================

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static SecurityAssignment convertCampusToAssignment(Campus campus) {
        int requiredPrestige = campus.getSecurityLevel();
        int requiredGuards = switch (requiredPrestige) {
            case 3 -> 5;
            case 2 -> 3;
            default -> 1;
        };

        return new SecurityAssignment(
                String.valueOf(campus.getCampusId()),
                campus.getName(),
                requiredPrestige,
                new HashSet<>(),
                requiredGuards
        );
    }
}