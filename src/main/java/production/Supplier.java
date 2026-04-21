package production;

import common.base.Selectable;

public class Supplier implements Selectable {
    private String id;
    private String name;
    private String contactEmail;
    private String phone;
    private boolean active;

    public Supplier(String id, String name, String contactEmail, String phone, boolean active) {
        this.id = id;
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.active = active;
    }

    @Override public String getId()   { return id; }
    @Override public String getName() { return name + (active ? "" : " [INACTIVE]"); }

    public String getContactEmail() { return contactEmail; }
    public String getPhone()        { return phone; }
    public boolean isActive()       { return active; }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s", id, name, contactEmail, active ? "Active" : "Inactive");
    }
}
