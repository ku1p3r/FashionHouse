package analytics;

import common.util.Terminal;
import common.wrapper.Option;

import java.util.List;

public class AnalyticsProgram {
    public static void main(String args[]){
        // TODO
        Option exit = new Option("back", "Quit to main screen.", () -> {});

        Terminal.prompt("Action", List.of(), List.of(
                /* TODO add other */
                exit
        ));
    }
}
