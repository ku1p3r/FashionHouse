package hr.ui;

import hr.ScreenInput;

/**
 * @author Mason Hart
 */
public interface Screen {

    /**
     * This method should display any necessary content on the screen, based on the screen's internal state
     */
    public void show() ;

    /**
     * This method should process user input and then do any necessary logic
     * It should be called immediately after show()
     * @return A command to exit the program, switch screens, or neither
     */
    public ScreenInput processInput() ;
}
