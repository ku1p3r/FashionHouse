package production;

import catalog.service.CatalogService;
import common.base.iScreen;
import common.util.Terminal;
import hr.model.Employee;

public class ProductionSelectionScreen implements iScreen {

    private final CatalogService service;
    private static final String REQUIRED_ROLE = "Production Manager";

    public ProductionSelectionScreen(CatalogService service) {
        this.service = service;
    }
    @Override
    public void run() {
        Terminal.clearScreen();
        printBanner();

        AuthService       auth     = new AuthService();
        MaterialRepository matRepo = new MaterialRepository();
        ProductionRepository prodRepo = new ProductionRepository(service);

        Employee user = auth.authenticate(REQUIRED_ROLE);
        if (user == null) {
            Terminal.printError("Authentication failed. Exiting.");
            return;
        }

        boolean running = true;
        while (running) {
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

            String choice = Terminal.prompt("Choice:");
            switch (choice.toLowerCase()) {
                case "1" -> {
                    MaterialModule matModule = new MaterialModule(matRepo, user);
                    matModule.run();
                }
                case "2" -> {
                    ProductionModule prodModule = new ProductionModule(prodRepo, matRepo, user);
                    prodModule.run();
                }
                case "back" -> {
                    running = false;
                }
                default -> {
                    Terminal.printError("Invalid option.");
                    Terminal.pressEnterToContinue();
                }
            }
        }

        Terminal.println();
        Terminal.printInfo("Goodbye!");
        Terminal.println();
    }

    private static void printBanner() {
        Terminal.printLine('=');
        System.out.println(Terminal.BOLD + Terminal.MAGENTA);
        System.out.println("        FASHIONHOUSE - MATERIAL SOURCING & PRODUCTION");
        System.out.println(Terminal.RESET);
        System.out.println(Terminal.DIM + Terminal.CYAN
            + "                  Material Sourcing & Production System"
            + Terminal.RESET);
        Terminal.printLine('=');
        Terminal.println();
    }
}
