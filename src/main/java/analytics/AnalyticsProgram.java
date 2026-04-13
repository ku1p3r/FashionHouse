package analytics;

import analytics.services.AnalyticsService;
import analytics.util.AnalyticsScreen;
import common.util.Terminal;
import common.wrapper.Option;

import java.util.List;

public class AnalyticsProgram {
    public static void main(String args[]){
        AnalyticsService analyticsService = new AnalyticsService();
        boolean[] running = {true};

        while(running[0]){
            Terminal.prompt("Action", List.of(), List.of(
                    new Option("check", "Check incoming monthly reports.", analyticsService::check),
                    new Option("log", "Log incoming stock.", analyticsService::log),
                    new Option("view", "Analytics summaries.", () -> {
                        new AnalyticsScreen(analyticsService);
                    }),
                    new Option("back", "Save and quit to main screen.", () -> running[0] = false)
            ));
        }
    }
}
