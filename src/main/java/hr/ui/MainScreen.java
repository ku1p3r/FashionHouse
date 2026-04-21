package hr.ui;

import hr.ScreenInput;
import hr.service.HRService;
import java.util.Scanner;

/**
 * @author Mason Hart
 */
public class MainScreen implements Screen {

    private HRService service;

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

        if(choice == 0){
            return ScreenInput.EXIT;
        } else if(choice == 1){
            return ScreenInput.TO_VIEW_APPLICATIONS;
        } else if(choice == 2){
            return ScreenInput.TO_CREATE_APPLICATION;
        } else if(choice == 3){
            return ScreenInput.TO_VIEW_EMPLOYEES;
        } else {
            return ScreenInput.NONE;
        }
    }
}
