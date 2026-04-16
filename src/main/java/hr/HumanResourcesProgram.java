package hr;

import common.base.Screen;
import hr.ui.MainScreen;

public class HumanResourcesProgram {

    public enum ScreenInput { NONE, EXIT }

    Screen mainScreen = new MainScreen();

    Screen currScreen = mainScreen;

    public static void main(String[] args){
        System.out.println("WIP");
    }

//    ScreenInput input = ScreenInput.NONE;
//    while(input != ScreenInput.EXIT){
//        currScreen.show();
//    }

}
