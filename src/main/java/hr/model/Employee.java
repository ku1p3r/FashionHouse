package hr.model;

import common.model.Timestamp;

/**
 * @author Mason Hart
 */
public class Employee {

    public static String[] DEPARTMENTS = new String[]{
            "SALES", "ANALYTICS", "PRODUCTION", "LEGAL", "ADVERTISING", "HR", "SECURITY", "OTHER"};

    private long id;
    private String name;
    private String title;
    private String dept;
    private int salary;
    private Timestamp startDate;
    private boolean isActive;
    private String pin;

    public Employee(
            long id, String name, String title, String dept, int salary, Timestamp startDate, boolean isActive, String pin
    ) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.dept = dept;
        this.salary = salary;
        this.startDate = startDate;
        this.isActive = isActive;
        this.pin = pin;

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDept() {
        return dept;
    }

    public int getSalary() {
        return salary;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean checkPin(String pin) {
        return pin.equals(this.pin);
    }

    public String toString(){
        return String.format("%d | %s | %s @ %s Dept | Salary: $%d,000 | start: %s | active: %b | pin: %s",
                id, name, title, dept, salary, startDate.toString(), isActive(), pin);
    }

}
