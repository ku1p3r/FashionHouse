package hr.ui;

import hr.ScreenInput;
import hr.service.HRService;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Mason Hart
 */
public class MainScreen implements Screen {

    private HRService service;

    private static final Map<Integer, ScreenInput> ACTION_MAP = Map.of(
            0, ScreenInput.EXIT,
            1, ScreenInput.TO_VIEW_APPLICATIONS,
            2, ScreenInput.TO_CREATE_APPLICATION,
            3, ScreenInput.TO_VIEW_EMPLOYEES
    );

    public MainScreen(HRService service){
        this.service = service;
    }

    @Override
    public void show() {
        System.out.print("Main Screen\n\n0:exit\n1:View Open Positions\n2:Create New Position\n3:View Employees\n\n---> ");
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();
        return ACTION_MAP.getOrDefault(choice, ScreenInput.NONE);
    }
}
