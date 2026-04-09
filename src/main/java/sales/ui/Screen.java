package sales.ui;

import sales.ScreenInput;

public interface Screen {

    public void show();

    public ScreenInput processInput();

    public Screen next();
}
