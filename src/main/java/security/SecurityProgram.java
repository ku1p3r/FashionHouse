package security;

import advertising.AdvertisingSystem;
import advertising.Event;

import java.util.List;

public class SecurityProgram {

    public static void main(String[] args) {

        List<SecurityGuard> guards = GuardFileManager.loadGuards();
        List<Campus> campuses = CampusFileManager.loadCampuses();

        AdvertisingSystem advertisingSystem = new AdvertisingSystem();
        advertisingSystem.loadEventsFromFile();
        List<Event> events = advertisingSystem.viewEvents();

        SecurityService service = new SecurityService();

        new SecurityProgramScreen(guards, campuses, events, service).run();
    }
}
