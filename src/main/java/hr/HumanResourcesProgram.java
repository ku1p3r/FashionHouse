package hr;

import hr.model.Application;
import hr.ui.Screen;
import hr.service.HRService;
import hr.ui.*;
import java.util.Map;

/**
 * @author Mason Hart
 */
public class HumanResourcesProgram {

    private static HRService service = new HRService();

    private static Screen mainScreen           = new MainScreen(service);
    private static Screen candidateScreen      = new CandidateScreen(service);
    private static Screen createAppScreen      = new CreateApplicationScreen(service);
    private static Screen employeeListScreen   = new EmployeeListScreen(service);
    private static Screen viewEmployeeScreen   = new EmployeeScreen(service);
    private static Screen listAppsScreen       = new ListApplicationsScreen(service);
    private static Screen sendOfferScreen      = new SendOfferScreen(service);
    private static Screen sendRejectScreen     = new SendRejectionScreen(service);
    private static Screen viewAppScreen        = new ViewApplicationScreen(service);
    private static Screen viewCandidatesScreen = new ViewCandidatesScreen(service);

    /** Maps each navigation signal to the Screen it should activate. */
    private static final Map<ScreenInput, Screen> SCREEN_MAP = Map.of(
            ScreenInput.TO_MAIN,               mainScreen,
            ScreenInput.TO_CREATE_APPLICATION, createAppScreen,
            ScreenInput.TO_VIEW_APPLICATIONS,  listAppsScreen,
            ScreenInput.TO_EDIT_APPLICATION,   viewAppScreen,
            ScreenInput.TO_VIEW_CANDIDATES,    viewCandidatesScreen,
            ScreenInput.TO_CANDIDATE,          candidateScreen,
            ScreenInput.TO_SEND_OFFER,         sendOfferScreen,
            ScreenInput.TO_SEND_REJECT,        sendRejectScreen,
            ScreenInput.TO_VIEW_EMPLOYEES,     employeeListScreen,
            ScreenInput.TO_EMPLOYEE,           viewEmployeeScreen
    );

    private static Screen currScreen = mainScreen;

    public static void main(String[] args){
        ScreenInput input = ScreenInput.NONE;
        do {
            currScreen.show();
            input = currScreen.processInput();
            // Dispatch: look up next screen; NONE and EXIT leave currScreen unchanged
            currScreen = SCREEN_MAP.getOrDefault(input, currScreen);
        } while (input != ScreenInput.EXIT);
    }

    public static void requestNewPosition(Application a){
        service.createApplication(a);
    }
}
