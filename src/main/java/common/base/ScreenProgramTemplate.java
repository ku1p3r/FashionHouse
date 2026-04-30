package common.base;

/**
 * Template Method for CLI modules: render → read input → route → repeat until exit.
 *
 * @param <TScreen> Per-module navigation state (often a screen instance).
 * @param <TInput>  Input token from the user (enum, string, int, etc.).
 */
public abstract class ScreenProgramTemplate<TScreen, TInput> {

    protected abstract TScreen initialScreen();

    protected abstract void render(TScreen screen);

    protected abstract TInput readInput(TScreen screen);

    protected abstract TScreen nextScreen(TScreen current, TInput input);

    protected abstract boolean shouldExit(TInput input);

    public final void run() {
        TScreen current = initialScreen();
        TInput input;
        do {
            render(current);
            input = readInput(current);
            current = nextScreen(current, input);
        } while (!shouldExit(input));
    }
}
