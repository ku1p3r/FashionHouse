package catalog.ui;

import catalog.service.CatalogService;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A screen for selecting an existing store or creating a new one.
 */
public class StoreSelectionScreen {

    private static final String DATA_DIR = "stores";

    public CatalogService run() {
        Path dataDir = Path.of(DATA_DIR);
        try { Files.createDirectories(dataDir); }
        catch (IOException e) { /* ignore */ }

        while (true) {
            Terminal.clearScreen();
            Terminal.printHeader("Merchandise Manager  —  Select Store");
            Terminal.println();

            List<Path> stores = discoverStores(dataDir);
            if (stores.isEmpty()) {
                Terminal.printInfo("No catalog files found in ./" + DATA_DIR + "/");
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
            Terminal.printMenuOption("(1-" + stores.size() + ")",    "Open an existing store");
            Terminal.printMenuOption("new",  "Create a new store");
            Terminal.printMenuOption("quit", "Exit");
            Terminal.println();

            String input = Terminal.prompt("Choice >");

            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("q")) return null;

            if (input.equalsIgnoreCase("new")) {
                CatalogService svc = createStore(dataDir);
                if (svc != null) return svc;
                continue;
            }

            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < stores.size()) {
                    return loadStore(stores.get(index));
                } else {
                    Terminal.printError("Invalid selection.");
                    Terminal.pressEnterToContinue();
                }
            } catch (NumberFormatException e) {
                Path direct = dataDir.resolve(input.endsWith(".catalog") ? input : input + ".catalog");
                return loadStore(direct);
            }
        }
    }

    private List<Path> discoverStores(Path dataDir) {
        List<Path> result = new ArrayList<>();
        try (var stream = Files.list(dataDir)) {
            stream.filter(p -> p.toString().endsWith(".catalog"))
                  .sorted()
                  .forEach(result::add);
        } catch (IOException e) { /* empty */ }
        return result;
    }

    private CatalogService loadStore(Path path) {
        try {
            CatalogService svc = new CatalogService(path);
            Terminal.printSuccess("Loaded: " + path.getFileName() + " (" + svc.size() + " products)");
            Terminal.pressEnterToContinue();
            return svc;
        } catch (IOException e) {
            Terminal.printError("Failed to load " + path + ": " + e.getMessage());
            Terminal.pressEnterToContinue();
            return null;
        }
    }

    private CatalogService createStore(Path dataDir) {
        Terminal.println();
        String name = Terminal.prompt("Store name >");
        if (name.isBlank()) return null;

        // Sanitize into a filename
        String filename = name.toLowerCase()
                              .replaceAll("[^a-z0-9]+", "_")
                              .replaceAll("^_|_$", "")
                + ".catalog";

        Path storePath = dataDir.resolve(filename);
        if (Files.exists(storePath)) {
            Terminal.printWarning("A store file named '" + filename + "' already exists.");
            if (!Terminal.confirm("Open it?")) return null;
        }

        return loadStore(storePath);
    }
}
