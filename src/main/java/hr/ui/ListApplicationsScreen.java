package hr.ui;

import hr.ScreenInput;
import hr.model.Application;
import hr.service.HRService;

import java.util.List;
import java.util.Scanner;

public class ListApplicationsScreen implements Screen {

    private HRService service;

    public ListApplicationsScreen(HRService service){
        this.service = service;
    }


    @Override
    public void show() {
        System.out.print("Application List\n\n");
        List<Application> apps = service.getApplications();
        for(int i = 0; i < apps.size(); i++){
            System.out.printf("%d: %s\n", i+1, apps.get(i).toString());
        }
        System.out.printf("\n0:main menu\n[1-%d]:select application\n---> ", apps.size());
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();

        // Named shortcut
        if (choice == 0) return ScreenInput.TO_MAIN;

        // Range-based selection
        int size = service.getApplications().size();
        if (choice >= 1 && choice <= size) {
            service.selectApplication(choice);
            return ScreenInput.TO_EDIT_APPLICATION;
        }

        return ScreenInput.NONE;
    }
}
