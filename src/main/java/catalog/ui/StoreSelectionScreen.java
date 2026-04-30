package catalog.ui;

import catalog.service.CatalogService;
import common.util.Terminal;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A screen for selecting an existing store or creating a new one.
 */
public class StoreSelectionScreen {

    private static final String DATA_DIR = "stores";

    static String dataDirName() {
        return DATA_DIR;
    }

    public CatalogService run() {
        Path dataDir = Path.of(DATA_DIR);
        try {
            Files.createDirectories(dataDir);
        } catch (IOException e) { /* ignore */ }

        return new StoreSelectionMenuScreen(dataDir).runForResult();
    }

    static List<Path> discoverStores(Path dataDir) {
        List<Path> result = new ArrayList<>();
        try (var stream = Files.list(dataDir)) {
            stream.filter(p -> p.toString().endsWith(".catalog"))
                    .sorted()
                    .forEach(result::add);
        } catch (IOException e) { /* empty */ }
        return result;
    }

    static CatalogService loadStore(Path path) {
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

    static CatalogService createStore(Path dataDir) {
        Terminal.println();
        String name = Terminal.prompt("Store name >");
        if (name.isBlank()) return null;

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
