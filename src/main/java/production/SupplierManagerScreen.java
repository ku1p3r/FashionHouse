package production;

import common.base.ScreenProgramTemplate;
import common.util.Terminal;

/**
 * Supplier directory menu using {@link ScreenProgramTemplate}.
 */
final class SupplierManagerScreen extends ScreenProgramTemplate<Void, String> {

    private final SupplierManager manager;
    private boolean running = true;

    SupplierManagerScreen(SupplierManager manager) {
        this.manager = manager;
    }

    @Override
    protected Void initialScreen() {
        return null;
    }

    @Override
    protected void render(Void unused) {
        manager.drawSupplierMainMenu();
    }

    @Override
    protected String readInput(Void unused) {
        return Terminal.prompt("Choice:");
    }

    @Override
    protected Void nextScreen(Void current, String choice) {
        switch (choice.toLowerCase()) {
            case "add" -> manager.addSupplier();
            case "edit" -> manager.editSupplier();
            case "remove" -> manager.removeSupplier();
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
