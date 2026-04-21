package security.command;

import security.SecurityGuard;
import java.util.List;

/**
 * Concrete Command — displays all security guards with prestige and availability.
 * Corresponds to case 3 of the original switch statement.
 */
public class ViewGuardsCommand implements Command {

    private final List<SecurityGuard> guards;

    public ViewGuardsCommand(List<SecurityGuard> guards) {
        this.guards = guards;
    }

    @Override
    public void execute() {
        System.out.println("\n--- GUARDS ---");
        for (int i = 0; i < guards.size(); i++) {
            SecurityGuard g = guards.get(i);
            System.out.println(i + ": " + g.getName()
                    + " | Prestige: " + g.getPrestigeLevel()
                    + " | Available: " + g.isAvailable());
        }
    }
}

