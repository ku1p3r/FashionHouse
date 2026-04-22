package hr.ui;

import hr.ScreenInput;
import hr.model.Employee;
import hr.service.HRService;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Mason Hart
 */
public class EmployeeScreen implements Screen {

    private HRService service;

    private static final Map<Integer, ScreenInput> ACTION_MAP = Map.of(
            0, ScreenInput.TO_MAIN,
            1, ScreenInput.TO_VIEW_EMPLOYEES
    );

    public EmployeeScreen(HRService service){
        this.service = service;
    }

    @Override
    public void show() {
        Employee e = service.getSelectedEmployee();
        System.out.printf("Employee #%d (%s)\n\n", e.getId(), e.getName());
        System.out.println("Title : " + e.getTitle());
        System.out.println("Dept  : " + e.getDept());
        System.out.printf( "Salary: $%d,000\n", e.getSalary());
        System.out.printf( "Start : %s\n", e.getStartDate().toString().split("T")[0]);
        System.out.printf( "Active: %b\n\n", e.isActive());
        System.out.print("0:main menu\n1:all employees\n\n---> ");
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();
        return ACTION_MAP.getOrDefault(choice, ScreenInput.NONE);
    }
}
