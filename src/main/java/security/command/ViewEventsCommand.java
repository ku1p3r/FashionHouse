package security.command;

import advertising.Event;
import java.util.List;

/**
 * Concrete Command — displays all events with their security levels.
 * Corresponds to case 1 of the original switch statement.
 */
public class ViewEventsCommand implements Command {

    private final List<Event> events;

    public ViewEventsCommand(List<Event> events) {
        this.events = events;
    }

    @Override
    public void execute() {
        System.out.println("\n--- EVENTS ---");
        for (int i = 0; i < events.size(); i++) {
            System.out.println(i + ": " + events.get(i).getName()
                    + " (Security Level: " + events.get(i).getSecurityLevel() + ")");
        }
    }
}

