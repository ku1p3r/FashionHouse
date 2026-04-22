package common.util;

import common.base.Command;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * String-keyed command dispatcher for interactive text menus.
 *
 * <p>Usage pattern:
 * <pre>
 *   MenuInvoker menu = new MenuInvoker("My Menu");
 *   menu.register("add",  "Add an item",  () -> doAdd());
 *   menu.register("back", "Return",        menu::stop);
 *
 *   while (menu.isRunning()) {
 *       menu.printOptions();
 *       String choice = Terminal.prompt("Choice:");
 *       menu.execute(choice);
 *   }
 * </pre>
 *
 * <p>Keys are matched case-insensitively. If no command is registered
 * for the given key the invoker prints "Invalid option." and continues.
 */
public class MenuInvoker {

    /** Ordered (insertion-order) map so printOptions() respects registration order. */
    private final Map<String, Command> commands    = new LinkedHashMap<>();
    private final List<String[]>       optionLines = new ArrayList<>(); // {key, description}

    private boolean running = true;

    // ── Registration ──────────────────────────────────────────────────────────

    /**
     * Register a command together with the menu-option text that should be
     * printed when {@link #printOptions()} is called.
     */
    public MenuInvoker register(String key, String description, Command action) {
        commands.put(key.toLowerCase(), action);
        optionLines.add(new String[]{key, description});
        return this;           // fluent
    }

    /**
     * Register a command <em>without</em> adding a printed menu option.
     * Useful for "hidden" or default-fallback commands.
     */
    public MenuInvoker registerSilent(String key, Command action) {
        commands.put(key.toLowerCase(), action);
        return this;
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    /** Called by a command (typically the "back" / "exit" command) to end the loop. */
    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    // ── Dispatch ──────────────────────────────────────────────────────────────

    /**
     * Returns true if a command is registered for the given key (case-insensitive).
     * Useful when the caller needs to distinguish named commands from free-text input.
     */
    public boolean isRegistered(String key) {
        return key != null && commands.containsKey(key.toLowerCase());
    }

    /**
     * Execute the command registered for {@code key} (case-insensitive).
     * Prints an error and continues if the key is unknown.
     */
    public void execute(String key) {
        Command cmd = commands.get(key == null ? "" : key.toLowerCase());
        if (cmd != null) {
            cmd.execute();
        } else {
            Terminal.printError("Invalid option.");
            Terminal.pressEnterToContinue();
        }
    }

    // ── Display ───────────────────────────────────────────────────────────────

    /**
     * Print all registered (non-silent) menu options using
     * {@link Terminal#printMenuOption(String, String)}.
     */
    public void printOptions() {
        for (String[] opt : optionLines) {
            Terminal.printMenuOption(opt[0], opt[1]);
        }
    }
}


