package production;

import catalog.service.CatalogService;
import common.base.iScreen;
import common.util.MenuInvoker;
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

        MenuInvoker menu = new MenuInvoker();

        menu.register("1",    "Material Management  — Inventory, sourcing, and supplier management",
                () -> new MaterialModule(matRepo, user).run())
            .register("2",    "Production Management — Batch requests, status tracking, finished goods",
                () -> new ProductionModule(prodRepo, matRepo, user).run())
            .register("back", "Back to Catalog", menu::stop);

        while (menu.isRunning()) {
            Terminal.clearScreen();
            Terminal.printHeader("Material and Production Management");
            Terminal.printInfo("Welcome, " + user.getName() + " (" + user.getId() + ")");
            Terminal.println();
            menu.printOptions();
            Terminal.println();
            menu.execute(Terminal.prompt("Choice:"));
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
