package security;

import advertising.AdvertisingSystem;
import advertising.Event;
import security.service.*;

import java.util.List;

public class SecurityProgram {

    public static void main(String[] args) {
        GuardRepository guardRepository = new FileGuardRepositoryAdapter();
        CampusRepository campusRepository = new FileCampusRepositoryAdapter();
        List<SecurityGuard> guards = guardRepository.loadGuards();
        List<Campus> campuses = campusRepository.loadCampuses();

        AdvertisingSystem advertisingSystem = new AdvertisingSystem();
        advertisingSystem.loadEventsFromFile();
        List<Event> events = advertisingSystem.viewEvents();

        SecurityService service = new SecurityService();

        new SecurityProgramScreen(guards, campuses, events, service, guardRepository, campusRepository).run();
    }
}
