package hr.ui;

import hr.ScreenInput;
import hr.model.Application;
import hr.service.HRService;

import java.util.Scanner;

/**
 * @author Mason Hart
 */
public class ViewApplicationScreen implements Screen {

    private HRService service;

    public ViewApplicationScreen(HRService service){
        this.service = service;
    }

    @Override
    public void show() {
        Application a = service.getSelectedApplication();
        System.out.printf("Application #%d\n\n", a.getId());
        System.out.printf("Title : %s\nDesc  : %s\nCloses: %s\nOpenings: %d\n\n",
                a.getTitle(), a.getDescription(), a.getCloseDate().toString(), a.getNumPositions());
        System.out.print("0:back\n1:view candidates\n---> ");
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();

        if(choice == 0){
            return ScreenInput.TO_VIEW_APPLICATIONS;
        } else if(choice == 1){
            return ScreenInput.TO_VIEW_CANDIDATES;
        } else {
            return ScreenInput.NONE;
        }
    }
}
