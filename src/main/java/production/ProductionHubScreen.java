package production;

import common.base.ScreenProgramTemplate;
import common.util.Terminal;
import hr.model.Employee;

/**
 * Material vs production submenu (after auth) using {@link ScreenProgramTemplate}.
 */
final class ProductionHubScreen extends ScreenProgramTemplate<Void, String> {

    private final MaterialRepository matRepo;
    private final ProductionRepository prodRepo;
    private final Employee user;
    private boolean running = true;

    ProductionHubScreen(MaterialRepository matRepo,
                        ProductionRepository prodRepo,
                        Employee user) {
        this.matRepo = matRepo;
        this.prodRepo = prodRepo;
        this.user = user;
    }

    @Override
    protected Void initialScreen() {
        return null;
    }

    @Override
    protected void render(Void unused) {
        Terminal.clearScreen();
        Terminal.printHeader("Material and Production Management");
        Terminal.printInfo("Welcome, " + user.getName() + " (" + user.getId() + ")");
        Terminal.println();
        Terminal.printMenuOption("1", "Material Management",
                "Inventory, sourcing, and supplier management");
        Terminal.printMenuOption("2", "Production Management",
                "Batch requests, status tracking, finished goods");
        Terminal.printMenuOption("back", "Back to Catalog");
        Terminal.println();
    }

    @Override
    protected String readInput(Void unused) {
        return Terminal.prompt("Choice:");
    }

    @Override
    protected Void nextScreen(Void current, String choice) {
        switch (choice.toLowerCase()) {
            case "1" -> {
                MaterialModule matModule = new MaterialModule(matRepo, user);
                matModule.run();
            }
            case "2" -> {
                ProductionModule prodModule = new ProductionModule(prodRepo, matRepo, user);
                prodModule.run();
            }
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
