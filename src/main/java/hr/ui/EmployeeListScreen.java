package hr.ui;

import hr.ScreenInput;
import hr.model.Employee;
import hr.service.HRService;

import java.util.List;
import java.util.Scanner;

/**
 * @author Mason Hart
 */
public class EmployeeListScreen implements Screen {

    private HRService service;
    private int numEmployees;

    private List<Employee> employeeList;

    public EmployeeListScreen(HRService service){
        this.service = service;
        employeeList = service.getEmployees();
        numEmployees = employeeList.size();
    }


    @Override
    public void show() {
        System.out.print("Employee List\n\n");
        int index = 0;
        for(int dept = 0; dept < Employee.DEPARTMENTS.length; dept++){
            System.out.printf("%s:\n", Employee.DEPARTMENTS[dept]);
            for (Employee emp : employeeList) {
                if (emp.getDept().equals(Employee.DEPARTMENTS[dept])) {
                    index++;
                    System.out.printf("%d: %s\n", emp.getId(), emp);
                }
            }
            System.out.println();
        }
        System.out.printf("0:main menu\n[1-%d]:select employee\n\n---> ", index);
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();

        // Named shortcut
        if (choice == 0) return ScreenInput.TO_MAIN;

        // Range-based selection
        if (choice >= 1 && choice <= numEmployees) {
            service.selectEmployee(choice);
            return ScreenInput.TO_EMPLOYEE;
        }

        return ScreenInput.NONE;
    }
}
