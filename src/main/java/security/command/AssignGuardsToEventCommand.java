package security.command;

import advertising.Event;
import security.SecurityAssignment;
import security.SecurityGuard;
import security.SecurityService;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * Concrete Command — interactively assigns guards to a selected event.
 * Corresponds to case 4 of the original switch statement.
 */
public class AssignGuardsToEventCommand implements Command {

    private final List<Event> events;
    private final List<SecurityGuard> guards;
    private final SecurityService service;
    private final Scanner scanner;

    public AssignGuardsToEventCommand(List<Event> events,
                                      List<SecurityGuard> guards,
                                      SecurityService service,
                                      Scanner scanner) {
        this.events = events;
        this.guards = guards;
        this.service = service;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        // Show events first
        System.out.println("\n--- EVENTS ---");
        for (int i = 0; i < events.size(); i++) {
            System.out.println(i + ": " + events.get(i).getName()
                    + " (Security Level: " + events.get(i).getSecurityLevel() + ")");
        }

        System.out.print("Select event index: ");
        int eventIndex = getIntInput();

        if (eventIndex < 0 || eventIndex >= events.size()) {
            System.out.println("Invalid event.");
            return;
        }

        Event selectedEvent = events.get(eventIndex);
        SecurityAssignment assignment = convertEventToAssignment(selectedEvent);

        System.out.println("\nAssigning guards for: " + selectedEvent.getName());

        List<SecurityGuard> qualified = service.getQualifiedGuards(assignment, guards);

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

            if (guardIndex == -1) break;

            if (guardIndex < 0 || guardIndex >= qualified.size()) {
                System.out.println("Invalid selection.");
                continue;
            }

            boolean success = service.assignGuard(assignment, qualified.get(guardIndex));
            if (!success) {
                System.out.println("Assignment failed.");
            }
        }

        service.finalizeAssignment(assignment);
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

    private int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}

