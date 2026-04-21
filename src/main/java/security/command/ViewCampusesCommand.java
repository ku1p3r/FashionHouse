package security.command;

import security.Campus;
import java.util.List;

/**
 * Concrete Command — displays all campuses.
 * Corresponds to case 2 of the original switch statement.
 */
public class ViewCampusesCommand implements Command {

    private final List<Campus> campuses;

    public ViewCampusesCommand(List<Campus> campuses) {
        this.campuses = campuses;
    }

    @Override
    public void execute() {
        System.out.println("\n--- CAMPUSES ---");
        for (int i = 0; i < campuses.size(); i++) {
            System.out.println(i + ": " + campuses.get(i));
        }
    }
}

