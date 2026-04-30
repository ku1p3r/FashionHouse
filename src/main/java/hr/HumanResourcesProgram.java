package hr;

import hr.model.Application;
import hr.service.HRService;

/**
 * @author Mason Hart
 */
public class HumanResourcesProgram {

    private static HRService service = new HRService();

    public static void main(String[] args) {
        new HRModuleScreen(service).run();
    }

    public static void requestNewPosition(Application a) {
        service.createApplication(a);
    }

}
