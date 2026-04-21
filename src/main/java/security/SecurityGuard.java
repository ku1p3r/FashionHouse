package security;

import common.model.Timestamp;
import hr.model.Employee;
import java.util.Random;
import java.util.Set;

public class SecurityGuard extends Employee {
    private int prestigeLevel;
    private Set<String> skills;
    private boolean available;

    public SecurityGuard(long id, String name, int prestigeLevel, Set<String> skills) {
        super(
            id,
            name,
            "Security Guard",
            "SECURITY",
            40000,
            new Timestamp(),
            true,
            String.valueOf(new Random().nextInt(0, 10000))
        );
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

    @Override
    public String getName() {
        return super.getName();
    }

    public long getId() {
        return super.getId();
    }
}