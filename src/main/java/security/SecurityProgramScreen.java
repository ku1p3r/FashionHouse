package security;

import advertising.Event;
import common.base.ScreenProgramTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * Security management menu using {@link ScreenProgramTemplate}.
 */
final class SecurityProgramScreen extends ScreenProgramTemplate<Void, Integer> {

    private final Scanner scanner = new Scanner(System.in);
    private final List<SecurityGuard> guards;
    private final List<Campus> campuses;
    private final List<Event> events;
    private final SecurityService service;
    private final GuardRepository guardRepository;
    private final CampusRepository campusRepository;

    SecurityProgramScreen(List<SecurityGuard> guards,
                          List<Campus> campuses,
                          List<Event> events,
                          SecurityService service,
                          GuardRepository guardRepository,
                          CampusRepository campusRepository) {
        this.guards = guards;
        this.campuses = campuses;
        this.events = events;
        this.service = service;
        this.guardRepository = guardRepository;
        this.campusRepository = campusRepository;
    }

    @Override
    protected Void initialScreen() {
        return null;
    }

    @Override
    protected void render(Void unused) {
        printMainMenu();
    }

    @Override
    protected Integer readInput(Void unused) {
        return getIntInput();
    }

    @Override
    protected Void nextScreen(Void current, Integer choice) {
        switch (choice) {
            case 1 -> viewEvents(events);
            case 2 -> viewCampuses(campuses);
            case 3 -> viewGuards(guards);
            case 4 -> assignGuardsToEvent(events, guards, service);
            case 5 -> assignGuardsToCampus(campuses, guards, service);
            case 6 -> {
                guardRepository.saveGuards(guards);
                campusRepository.saveCampuses(campuses);
                System.out.println("Saved. Exiting...");
            }
            default -> System.out.println("Invalid option");
        }
        return null;
    }

    @Override
    protected boolean shouldExit(Integer input) {
        return input == 6;
    }

    private void printMainMenu() {
        System.out.println("\n=== SECURITY MANAGEMENT SYSTEM ===");
        System.out.println("1. View Events");
        System.out.println("2. View Campuses");
        System.out.println("3. View Guards");
        System.out.println("4. Assign Guards to Event");
        System.out.println("5. Assign Guards to Campus");
        System.out.println("6. Save & Exit");
        System.out.print("Select option: ");
    }

    private void viewEvents(List<Event> events) {
        System.out.println("\n--- EVENTS ---");
        for (int i = 0; i < events.size(); i++) {
            System.out.println(i + ": " + events.get(i).getName()
                    + " (Security Level: " + events.get(i).getSecurityLevel() + ")");
        }
    }

    private void viewCampuses(List<Campus> campuses) {
        System.out.println("\n--- CAMPUSES ---");
        for (int i = 0; i < campuses.size(); i++) {
            System.out.println(i + ": " + campuses.get(i));
        }
    }

    private void viewGuards(List<SecurityGuard> guards) {
        System.out.println("\n--- GUARDS ---");
        for (int i = 0; i < guards.size(); i++) {
            SecurityGuard g = guards.get(i);
            System.out.println(i + ": " + g.getName()
                    + " | Prestige: " + g.getPrestigeLevel()
                    + " | Available: " + g.isAvailable());
        }
    }

    private void assignGuardsToEvent(List<Event> events,
                                     List<SecurityGuard> guards,
                                     SecurityService service) {

        viewEvents(events);
        System.out.print("Select event index: ");
        int eventIndex = getIntInput();

        if (eventIndex < 0 || eventIndex >= events.size()) {
            System.out.println("Invalid event.");
            return;
        }

        Event selectedEvent = events.get(eventIndex);
        SecurityAssignment assignment = convertEventToAssignment(selectedEvent);

        System.out.println("\nAssigning guards for: " + selectedEvent.getName());

        List<SecurityGuard> qualified =
                service.getQualifiedGuards(assignment, guards);

        if (qualified.isEmpty()) {
            System.out.println("No qualified guards available.");
            return;
        }

        while (assignment.needsMoreGuards()) {

            System.out.println("\nQualified Guards:");
            for (int i = 0; i < qualified.size(); i++) {
                SecurityGuard g = qualified.get(i);
                System.out.println(i + ": " + g.getName()
                        + " (Prestige " + g.getPrestigeLevel() + ")");
            }

            System.out.print("Select guard index to assign (-1 to stop): ");
            int guardIndex = getIntInput();

            if (guardIndex == -1) {
                break;
            }

            if (guardIndex < 0 || guardIndex >= qualified.size()) {
                System.out.println("Invalid selection.");
                continue;
            }

            SecurityGuard selectedGuard = qualified.get(guardIndex);

            boolean success = service.assignGuard(assignment, selectedGuard);

            if (!success) {
                System.out.println("Assignment failed.");
            }
        }

        service.finalizeAssignment(assignment);
    }

    private void assignGuardsToCampus(List<Campus> campuses,
                                    List<SecurityGuard> guards,
                                    SecurityService service) {

        viewCampuses(campuses);
        System.out.print("Select campus index: ");
        int index = getIntInput();

        if (index < 0 || index >= campuses.size()) {
            System.out.println("Invalid campus.");
            return;
        }

        Campus selected = campuses.get(index);

        SecurityAssignment assignment = convertCampusToAssignment(selected);

        System.out.println("\nAssigning guards to: " + selected.getName());

        List<SecurityGuard> qualified =
                service.getQualifiedGuards(assignment, guards);

        if (qualified.isEmpty()) {
            System.out.println("No qualified guards.");
            return;
        }

        for (SecurityGuard g : qualified) {
            service.assignGuard(assignment, g);
            if (!assignment.needsMoreGuards()) break;
        }

        service.finalizeAssignment(assignment);
    }

    private int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private SecurityAssignment convertEventToAssignment(Event event) {

        int requiredPrestige = event.getSecurityLevel();

        int requiredGuards = switch (requiredPrestige) {
            case 3 -> 5;
            case 2 -> 3;
            default -> 1;
        };

        return new SecurityAssignment(
                String.valueOf(event.getEventId()),
                event.getName(),
                requiredPrestige,
                new HashSet<>(),
                requiredGuards
        );
    }

    private SecurityAssignment convertCampusToAssignment(Campus campus) {

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
