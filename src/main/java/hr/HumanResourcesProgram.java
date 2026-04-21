package hr;

import hr.model.Application;
import hr.ui.Screen;
import hr.service.HRService;
import hr.ui.*;
import hr.ScreenInput;

/**
 * @author Mason Hart
 */
public class HumanResourcesProgram {

    private static HRService service = new HRService();

    private static Screen mainScreen = new MainScreen(service);
    private static Screen candidateScreen = new CandidateScreen(service);
    private static Screen createAppScreen = new CreateApplicationScreen(service);
    private static Screen employeeListScreen = new EmployeeListScreen(service);
    private static Screen viewEmployeeScreen = new EmployeeScreen(service);
    private static Screen listAppsScreen = new ListApplicationsScreen(service);
    private static Screen sendOfferScreen = new SendOfferScreen(service);
    private static Screen sendRejectScreen = new SendRejectionScreen(service);
    private static Screen viewAppScreen = new ViewApplicationScreen(service);
    private static Screen viewCandidatesScreen = new ViewCandidatesScreen(service);

    private static Screen currScreen = mainScreen;

    public static void main(String[] args){

        ScreenInput input = ScreenInput.NONE;
        while(input != ScreenInput.EXIT){

            currScreen.show();
            input = currScreen.processInput();

            switch(input){
                case TO_MAIN -> { currScreen = mainScreen; }
                case TO_CREATE_APPLICATION -> { currScreen = createAppScreen; }
                case TO_VIEW_APPLICATIONS -> { currScreen = listAppsScreen; }
                case TO_EDIT_APPLICATION -> { currScreen = viewAppScreen; }
                case TO_VIEW_CANDIDATES -> { currScreen = viewCandidatesScreen; }
                case TO_CANDIDATE -> { currScreen = candidateScreen; }
                case TO_SEND_OFFER -> { currScreen = sendOfferScreen; }
                case TO_SEND_REJECT -> { currScreen = sendRejectScreen; }
                case TO_VIEW_EMPLOYEES -> { currScreen = employeeListScreen; }
                case TO_EMPLOYEE -> { currScreen = viewEmployeeScreen; }
                case NONE -> { /* do nothing */ }
                case EXIT -> { System.exit(0); }
            }

        }

    }


    public static void requestNewPosition(Application a){
        service.createApplication(a);
    }

}
