package production;

import common.base.ScreenProgramTemplate;
import common.util.Terminal;
import hr.model.Employee;

/**
 * Top-level production management menu using {@link ScreenProgramTemplate}.
 */
final class ProductionMainScreen extends ScreenProgramTemplate<Void, String> {

    private final ProductionModule module;
    private final Employee currentUser;
    private boolean running = true;

    ProductionMainScreen(ProductionModule module, Employee currentUser) {
        this.module = module;
        this.currentUser = currentUser;
    }

    @Override
    protected Void initialScreen() {
        return null;
    }

    @Override
    protected void render(Void unused) {
        module.drawProductionMainMenu(currentUser);
    }

    @Override
    protected String readInput(Void unused) {
        return Terminal.prompt("Choice:");
    }

    @Override
    protected Void nextScreen(Void current, String choice) {
        switch (choice) {
            case "1" -> module.viewAllBatches();
            case "2" -> module.createBatch("");
            case "3" -> module.updateBatchStatus();
            case "back" -> running = false;
            default -> {
                Terminal.printError("Invalid option.");
                Terminal.pressEnterToContinue();
            }
        }
        return null;
    }

    @Override
    protected boolean shouldExit(String input) {
        return !running;
    }
}
