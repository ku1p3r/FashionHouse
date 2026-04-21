package hr.ui;

import common.model.Timestamp;
import hr.ScreenInput;
import hr.model.Application;
import hr.model.Employee;
import hr.service.HRService;

import java.util.Scanner;

/**
 * @author Mason Hart
 */
public class CreateApplicationScreen implements Screen {

    /**
     * 0 - title
     * 1 - desc
     * 2 - dept
     * 3 - set close date
     * 4 - # open positions
     */
    int stage = 0;

    private HRService service;

    private Application newApp;

    private boolean enterInput;

    public CreateApplicationScreen(HRService service){
        this.newApp = new Application();
        this.service = service;
        this.enterInput = false;
    }


    @Override
    public void show() {

        if(!enterInput){
            System.out.print("Create New Application\n\n");
            System.out.printf("Title : %s\nDesc  : %s\nDept  : %s\nCloses: %s\nNum   : %d\n\n",
                    newApp.getTitle(), newApp.getDescription(), newApp.getDept(), newApp.getCloseDate(), newApp.getNumPositions());
            System.out.printf("0:main menu\n1:view applications\n2:edit\n3:submit\n---> ");
        } else {
            switch(stage){
                case 0 -> {
                    System.out.print("Enter job title ---> ");
                }
                case 1 -> {
                    System.out.print("Enter job description ---> ");
                }
                case 2 -> {
                    System.out.print("0: SALES\n1: ANALYTICS\n2: PRODUCTION\n3: LEGAL\n4: ADVERTISING\n5: HR\n6: SECURITY\n7: OTHER\nEnter department ---> ");
                }
                case 3 -> {
                    System.out.print("Enter close date ---> ");
                }
                case 4 -> {
                    System.out.print("Number of positions offered ---> ");
                }
            }
        }
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        if(!enterInput){
            int choice = scn.nextInt();

            if(choice == 0){
                newApp = new Application();
                return ScreenInput.TO_MAIN;
            } else if(choice == 1){
                newApp = new Application();
                return ScreenInput.TO_VIEW_APPLICATIONS;
            } else if(choice == 2){

                stage = 0;
                enterInput = true;
                return ScreenInput.NONE;
            } else if(choice == 3){

                service.createApplication(newApp);
                this.newApp = new Application();

                return ScreenInput.TO_EDIT_APPLICATION;
            } else {
                return ScreenInput.NONE;
            }
        } else {
            String input = scn.nextLine();
            switch(stage){
                case 0 -> { newApp.setTitle(input); stage = 1; }
                case 1 -> { newApp.setDescription(input); stage = 2; }
                case 2 -> { newApp.setDept(Employee.DEPARTMENTS[Integer.parseInt(input)]); stage = 3; }
                case 3 -> { newApp.setCloseDate(new Timestamp(input + "T00:00:00")); stage = 4; }
                case 4 -> { newApp.setNumPositions(Integer.parseInt(input)); stage = 0; enterInput = false; }
            }
        }
        return ScreenInput.NONE;
    }
}
