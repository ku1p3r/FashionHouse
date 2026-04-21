package security;

import java.util.Set;

public class SecurityGuard extends Employee{
    private int prestigeLevel;
    private Set<String> skills;
    private boolean available;

    public SecurityGuard(long id, String name, int prestigeLevel, Set<String> skills) {
        super(id, name, "Security Guard", "SECURITY", 40000, null, true)
        this.prestigeLevel = prestigeLevel;
        this.skills = skills;
        this.available = true;
    }

    public int getPrestigeLevel() {
        return prestigeLevel;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public boolean isAvailable() {
        return available;
    }

    public void assign() {
        this.available = false;
    }

    public void unassign() {
        this.available = true;
    }

    public String getName() {
        return name;
    }

    public String getId() {
    return id;
}
}