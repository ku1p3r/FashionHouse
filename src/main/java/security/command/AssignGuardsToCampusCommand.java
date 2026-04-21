package security.command;

import security.Campus;
import security.SecurityAssignment;
import security.SecurityGuard;
import security.SecurityService;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * Concrete Command — automatically assigns qualified guards to a selected campus.
 * Corresponds to case 5 of the original switch statement.
 */
public class AssignGuardsToCampusCommand implements Command {

    private final List<Campus> campuses;
    private final List<SecurityGuard> guards;
    private final SecurityService service;
    private final Scanner scanner;

    public AssignGuardsToCampusCommand(List<Campus> campuses,
                                       List<SecurityGuard> guards,
                                       SecurityService service,
                                       Scanner scanner) {
        this.campuses = campuses;
        this.guards = guards;
        this.service = service;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        // Show campuses first
        System.out.println("\n--- CAMPUSES ---");
        for (int i = 0; i < campuses.size(); i++) {
            System.out.println(i + ": " + campuses.get(i));
        }

        System.out.print("Select campus index: ");
        int index = getIntInput();

        if (index < 0 || index >= campuses.size()) {
            System.out.println("Invalid campus.");
            return;
        }

        Campus selected = campuses.get(index);
        SecurityAssignment assignment = convertCampusToAssignment(selected);

        System.out.println("\nAssigning guards to: " + selected.getName());

        List<SecurityGuard> qualified = service.getQualifiedGuards(assignment, guards);

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

    private int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}

