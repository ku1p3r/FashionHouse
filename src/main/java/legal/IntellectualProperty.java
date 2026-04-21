package legal;

import java.util.UUID;


public class IntellectualProperty {
    private UUID id;
    private String name;
    private boolean exclusivelyLicensed;

    public IntellectualProperty(UUID id, String name) {
        this.id = id;
        this.name = name;
        this.exclusivelyLicensed = false;
    }

    public UUID getId() { return id; }

    public String getName() { return name; }

    public boolean isExclusivelyLicensed() {
        return exclusivelyLicensed;
    }

    public void markAsExclusivelyLicensed() {
        this.exclusivelyLicensed = true;
    }
}
