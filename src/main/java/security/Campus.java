package security;

public class Campus {

    private int campusId;
    private String name;
    private String location;
    private int securityLevel;

    public Campus(int campusId, String name, String location, int securityLevel) {
        this.campusId = campusId;
        this.name = name;
        this.location = location;
        this.securityLevel = securityLevel;
    }

    public int getCampusId() { return campusId; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getSecurityLevel() { return securityLevel; }

    @Override
    public String toString() {
        return name + " (" + location + ") - Security Level: " + securityLevel;
    }
}
