package production;

import common.util.Serializer;
import common.util.Terminal;
import java.util.ArrayList;
import java.util.List;

public class AuthService {

    private List<Employee> employees = new ArrayList<>();

    public AuthService() {
        load();
    }

    private void load() {
        try {
            Serializer s = new Serializer(DataPaths.EMPLOYEES);
            for (int i = 0; i < s.size(); i++) {
                employees.add(new Employee(
                        s.get("id",   i, String.class),
                        s.get("name", i, String.class),
                        s.get("pin",  i, String.class),
                        s.get("role", i, String.class)
                ));
            }
        } catch (Exception e) {
            Terminal.printError("Could not load employee data: " + e.getMessage());
        }
    }

    /**
     * Prompts for an employee ID and PIN, validates, and returns the authenticated
     * Employee if credentials match and the employee has the required role.
     * Returns null if authentication fails or the user cancels (types "back").
     */
    public Employee authenticate(String requiredRole) {
        Terminal.printSubHeader("Authentication Required");
        Terminal.printInfo("Enter your Employee ID and PIN to access this module.");
        Terminal.println();

        for (int attempts = 0; attempts < 3; attempts++) {
            String empId = Terminal.prompt("Employee ID (or 'back' to cancel):");
            if (empId.equalsIgnoreCase("back")) return null;

            Employee emp = findById(empId);
            if (emp == null) {
                Terminal.printError("Employee ID not found.");
                continue;
            }

            String pin = Terminal.prompt("PIN:");
            if (!emp.checkPin(pin)) {
                Terminal.printError("Incorrect PIN. " + (2 - attempts) + " attempt(s) remaining.");
                continue;
            }

            if (requiredRole != null && !emp.getRole().equalsIgnoreCase(requiredRole)) {
                Terminal.printError("Access denied. Required role: " + requiredRole);
                Terminal.printInfo("Your role: " + emp.getRole());
                return null;
            }

            Terminal.printSuccess("Authenticated as " + emp.getName() + " (" + emp.getId() + ")");
            return emp;
        }

        Terminal.printError("Too many failed attempts. Access denied.");
        return null;
    }

    private Employee findById(String id) {
        for (Employee e : employees) {
            if (e.getId().equalsIgnoreCase(id)) return e;
        }
        return null;
    }

    public List<Employee> getAll() { return employees; }
}
