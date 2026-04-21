package security.command;

import java.util.HashMap;
import java.util.Map;

/**
 * CommandInvoker maps integer menu choices to Command objects and executes them.
 * Replaces the switch statement in SecurityProgram.
 */
public class CommandInvoker {

    private final Map<Integer, Command> commandMap = new HashMap<>();
    private boolean running = true;

    /**
     * Register a command for a given menu choice key.
     */
    public void register(int key, Command command) {
        commandMap.put(key, command);
    }

    /**
     * Execute the command associated with the given key.
     * Prints an error message if no command is registered for the key.
     */
    public void execute(int key) {
        Command command = commandMap.get(key);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Invalid option");
        }
    }

    /** Called by commands (e.g. SaveAndExitCommand) to stop the main loop. */
    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}

