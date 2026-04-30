package hr;

import hr.model.Application;
import hr.service.HRDataRepository;
import hr.service.HRService;
import hr.service.SerializerHRDataRepositoryAdapter;

/**
 * @author Mason Hart
 */
public class HumanResourcesProgram {

    private static final HRDataRepository repository = new SerializerHRDataRepositoryAdapter();
    private static HRService service = new HRService(repository);

    public static void main(String[] args) {
        new HRModuleScreen(service).run();
    }

    public static void requestNewPosition(Application a) {
        service.createApplication(a);
    }

}
