package production;

import common.base.ScreenProgramTemplate;
import common.util.Terminal;
import hr.model.Employee;

import java.util.List;

/**
 * Top-level material management menu using {@link ScreenProgramTemplate}.
 */
final class MaterialMainScreen extends ScreenProgramTemplate<Void, String> {

    private final MaterialModule module;
    private final Employee currentUser;
    private boolean running = true;

    MaterialMainScreen(MaterialModule module, Employee currentUser) {
        this.module = module;
        this.currentUser = currentUser;
    }

    @Override
    protected Void initialScreen() {
        return null;
    }

    @Override
    protected void render(Void unused) {
        Terminal.clearScreen();
        Terminal.printHeader("MATERIAL MANAGEMENT");
        Terminal.printInfo("Logged in as: " + currentUser.getName() + " (" + currentUser.getId() + ")");
        Terminal.println();

        List<Material> lowStock = module.getRepo().getLowStock();
        if (!lowStock.isEmpty()) {
            Terminal.printWarning("LOW STOCK ALERT: " + lowStock.size() + " material(s) below reorder threshold!");
            for (Material m : lowStock) {
                Terminal.printWarning("  " + m.getId() + " – " + m.getName()
                        + ": " + m.getQuantity() + " " + m.getUnit()
                        + " (threshold: " + m.getReorderThreshold() + ")");
            }
            Terminal.println();
        }

        module.printDashboard();
        Terminal.printSubHeader("Material Management Menu");
        Terminal.printMenuOption("(ID/Keyword)", "Browse / Search Materials");
        Terminal.printMenuOption("new", "Add New Material");
        Terminal.printMenuOption("suppliers", "View Supplier Directory");
        Terminal.printMenuOption("back", "Return to Main Menu");
        Terminal.println();
    }

    @Override
    protected String readInput(Void unused) {
        return Terminal.prompt("Choice:");
    }

    @Override
    protected Void nextScreen(Void current, String choice) {
        switch (choice) {
            case "new" -> module.addNewMaterial();
            case "suppliers" -> module.viewSuppliers();
            case "back" -> running = false;
            default -> module.browseMaterials(choice);
        }
        return null;
    }

    @Override
    protected boolean shouldExit(String input) {
        return !running;
    }
}
