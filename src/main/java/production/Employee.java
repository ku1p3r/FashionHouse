package production;

public class Employee {
    private String id;
    private String name;
    private String pin;
    private String role;

    public Employee(String id, String name, String pin, String role) {
        this.id = id;
        this.name = name;
        this.pin = pin;
        this.role = role;
    }

    public String getId()   { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }

    public boolean checkPin(String input) { return pin.equals(input); }

    @Override
    public String toString() { return id + " | " + name + " | " + role; }
}
