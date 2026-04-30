package hr;

import common.base.ScreenProgramTemplate;
import hr.service.HRService;
import hr.ui.*;

/**
 * Human Resources console flow using {@link ScreenProgramTemplate}.
 */
final class HRModuleScreen extends ScreenProgramTemplate<Screen, ScreenInput> {

    private final Screen mainScreen;
    private final Screen candidateScreen;
    private final Screen createAppScreen;
    private final Screen employeeListScreen;
    private final Screen viewEmployeeScreen;
    private final Screen listAppsScreen;
    private final Screen sendOfferScreen;
    private final Screen sendRejectScreen;
    private final Screen viewAppScreen;
    private final Screen viewCandidatesScreen;

    HRModuleScreen(HRService service) {
        mainScreen = new MainScreen(service);
        candidateScreen = new CandidateScreen(service);
        createAppScreen = new CreateApplicationScreen(service);
        employeeListScreen = new EmployeeListScreen(service);
        viewEmployeeScreen = new EmployeeScreen(service);
        listAppsScreen = new ListApplicationsScreen(service);
        sendOfferScreen = new SendOfferScreen(service);
        sendRejectScreen = new SendRejectionScreen(service);
        viewAppScreen = new ViewApplicationScreen(service);
        viewCandidatesScreen = new ViewCandidatesScreen(service);
    }

    @Override
    protected Screen initialScreen() {
        return mainScreen;
    }

    @Override
    protected void render(Screen screen) {
        screen.show();
    }

    @Override
    protected ScreenInput readInput(Screen screen) {
        return screen.processInput();
    }

    @Override
    protected Screen nextScreen(Screen current, ScreenInput input) {
        return switch (input) {
            case TO_MAIN -> mainScreen;
            case TO_CREATE_APPLICATION -> createAppScreen;
            case TO_VIEW_APPLICATIONS -> listAppsScreen;
            case TO_EDIT_APPLICATION -> viewAppScreen;
            case TO_VIEW_CANDIDATES -> viewCandidatesScreen;
            case TO_CANDIDATE -> candidateScreen;
            case TO_SEND_OFFER -> sendOfferScreen;
            case TO_SEND_REJECT -> sendRejectScreen;
            case TO_VIEW_EMPLOYEES -> employeeListScreen;
            case TO_EMPLOYEE -> viewEmployeeScreen;
            case NONE -> current;
            default -> current;
        };
    }

    @Override
    protected boolean shouldExit(ScreenInput input) {
        return input == ScreenInput.EXIT;
    }
}
