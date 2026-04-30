package catalog.ui;

import catalog.service.CatalogService;
import common.base.ScreenProgramTemplate;
import common.util.Terminal;

import java.nio.file.Path;
import java.util.List;

/**
 * Store picker loop using {@link ScreenProgramTemplate}.
 */
final class StoreSelectionMenuScreen extends ScreenProgramTemplate<Void, String> {

    private final Path dataDir;
    private CatalogService outcome;
    private boolean cancelled;
    private boolean finished;

    StoreSelectionMenuScreen(Path dataDir) {
        this.dataDir = dataDir;
    }

    CatalogService runForResult() {
        run();
        return cancelled ? null : outcome;
    }

    @Override
    protected Void initialScreen() {
        return null;
    }

    @Override
    protected void render(Void unused) {
        Terminal.clearScreen();
        Terminal.printHeader("Merchandise Manager  —  Select Store");
        Terminal.println();

        List<Path> stores = StoreSelectionScreen.discoverStores(dataDir);
        if (stores.isEmpty()) {
            Terminal.printInfo("No catalog files found in ./" + StoreSelectionScreen.dataDirName() + "/");
        } else {
            Terminal.printSubHeader("Available Stores");
            for (int i = 0; i < stores.size(); i++) {
                String filename = stores.get(i).getFileName().toString();
                String storeName = filename.replace(".catalog", "").replace("_", " ");
                Terminal.printTableRow(i + 1, storeName, filename);
            }
        }

        Terminal.println();
        Terminal.printLine();
        Terminal.printMenuOption("(1-" + stores.size() + ")", "Open an existing store");
        Terminal.printMenuOption("new", "Create a new store");
        Terminal.printMenuOption("back", "Leave Catalog");
        Terminal.println();
    }

    @Override
    protected String readInput(Void unused) {
        return Terminal.prompt("Choice >");
    }

    @Override
    protected Void nextScreen(Void current, String input) {
        if (input.equalsIgnoreCase("back")) {
            cancelled = true;
            finished = true;
            return null;
        }

        if (input.equalsIgnoreCase("new")) {
            CatalogService svc = StoreSelectionScreen.createStore(dataDir);
            if (svc != null) {
                outcome = svc;
                finished = true;
            }
            return null;
        }

        try {
            List<Path> stores = StoreSelectionScreen.discoverStores(dataDir);
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < stores.size()) {
                CatalogService svc = StoreSelectionScreen.loadStore(stores.get(index));
                if (svc != null) {
                    outcome = svc;
                    finished = true;
                }
            } else {
                Terminal.printError("Invalid selection.");
                Terminal.pressEnterToContinue();
            }
        } catch (NumberFormatException e) {
            Terminal.printError("Invalid selection.");
            Terminal.pressEnterToContinue();
        }
        return null;
    }

    @Override
    protected boolean shouldExit(String input) {
        return finished;
    }
}
