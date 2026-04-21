package security.command;

import security.Campus;
import security.CampusFileManager;
import security.GuardFileManager;
import security.SecurityGuard;
import java.util.List;

/**
 * Concrete Command — persists guards and campuses to disk, then stops the main loop.
 * Corresponds to case 6 of the original switch statement.
 */
public class SaveAndExitCommand implements Command {

    private final List<SecurityGuard> guards;
    private final List<Campus> campuses;
    private final CommandInvoker invoker;

    public SaveAndExitCommand(List<SecurityGuard> guards,
                              List<Campus> campuses,
                              CommandInvoker invoker) {
        this.guards = guards;
        this.campuses = campuses;
        this.invoker = invoker;
    }

    @Override
    public void execute() {
        GuardFileManager.saveGuards(guards);
        CampusFileManager.saveCampuses(campuses);
        System.out.println("Saved. Exiting...");
        invoker.stop();
    }
}

