package security;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SecurityAssignment {
    private String id;
    private String name;
    private int requiredPrestige;
    private Set<String> requiredSkills;
    private int requiredGuards;
    private List<SecurityGuard> assignedGuards;

    public SecurityAssignment(String id, String name, int requiredPrestige,
                              Set<String> requiredSkills, int requiredGuards) {
        this.id = id;
        this.name = name;
        this.requiredPrestige = requiredPrestige;
        this.requiredSkills = requiredSkills;
        this.requiredGuards = requiredGuards;
        this.assignedGuards = new ArrayList<>();
    }

    public int getRequiredPrestige() {
        return requiredPrestige;
    }

    public Set<String> getRequiredSkills() {
        return requiredSkills;
    }

    public int getRequiredGuards() {
        return requiredGuards;
    }

    public List<SecurityGuard> getAssignedGuards() {
        return assignedGuards;
    }

    public void addGuard(SecurityGuard guard) {
        assignedGuards.add(guard);
        guard.assign();
    }

    public boolean needsMoreGuards() {
        return assignedGuards.size() < requiredGuards;
    }

    public String getName() {
        return name;
    }
}