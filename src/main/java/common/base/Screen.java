package common.base;

import sales.ScreenInput;

/**
 * @author Mason Hart
 */
public interface Screen {

    /**
     * Display the contents of the screen
     */
    public void show();

    /**
     * Read user input, then return a status code to the Main System so that it knows
     * what to do next, if anything
     * @return ScreenInput enum representing the type of input received by the screen
     */
    public ScreenInput processInput();

}
