package security.service;

import security.SecurityAssignment;
import security.SecurityGuard;

import java.util.List;
import java.util.stream.Collectors;

public class SecurityService {

    // Step 5: Filter qualified guards
    public List<SecurityGuard> getQualifiedGuards(SecurityAssignment assignment,
                                                  List<SecurityGuard> guards) {

        return guards.stream()
                .filter(g -> g.isAvailable())
                .filter(g -> g.getPrestigeLevel() >= assignment.getRequiredPrestige())
                .filter(g -> g.getSkills().containsAll(assignment.getRequiredSkills()))
                .collect(Collectors.toList());
    }

    // Step 6: Assign guard
    public boolean assignGuard(SecurityAssignment assignment, SecurityGuard guard) {

        // Alternate Flow 6a: Scheduling conflict
        if (!guard.isAvailable()) {
            System.out.println("Conflict: Guard already assigned.");
            return false;
        }

        // Alternate Flow 5d: Override (guard doesn't meet requirements)
        if (guard.getPrestigeLevel() < assignment.getRequiredPrestige()) {
            System.out.println("WARNING: Assigning underqualified guard (override logged).");
        }

        assignment.addGuard(guard);
        System.out.println("Assigned " + guard.getName() + " to " + assignment.getName());

        return true;
    }

    // Step 7: Confirm assignment
    public void finalizeAssignment(SecurityAssignment assignment) {

        if (assignment.needsMoreGuards()) {
            System.out.println("Warning: More guards are needed.");
        } else {
            System.out.println("Assignment complete.");
        }
    }

    // Alternate Flow 7a: Guard becomes unavailable
    public void handleGuardUnavailable(SecurityAssignment assignment, SecurityGuard guard) {
        assignment.getAssignedGuards().remove(guard);
        guard.unassign();

        System.out.println("Guard removed. Reassignment needed.");
    }
}
