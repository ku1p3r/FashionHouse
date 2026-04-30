package hr;

import hr.model.Application;
import hr.service.HRDataRepository;
import hr.service.HRService;
import hr.service.HumanResourcesAdapter;

/**
 * @author Mason Hart
 */
public class HumanResourcesProgram {

    private static final HRDataRepository repository = new HumanResourcesAdapter();
    private static HRService service = new HRService(repository);

    public static void main(String[] args) {
        new HRModuleScreen(service).run();
    }

    public static void requestNewPosition(Application a) {
        service.createApplication(a);
    }

}
