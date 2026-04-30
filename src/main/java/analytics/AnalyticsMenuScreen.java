package analytics;

import analytics.services.AnalyticsService;
import analytics.ui.AnalyticsScreen;
import analytics.ui.TrendScreen;
import common.base.ScreenProgramTemplate;
import common.util.Terminal;
import common.wrapper.Option;

import java.util.List;

/**
 * Analytics top-level menu using {@link ScreenProgramTemplate}.
 */
final class AnalyticsMenuScreen extends ScreenProgramTemplate<Void, Void> {

    private final AnalyticsService analyticsService;
    private final boolean[] running = {true};

    AnalyticsMenuScreen(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Override
    protected Void initialScreen() {
        return null;
    }

    @Override
    protected void render(Void unused) {
        // Menu is drawn inside Terminal.prompt
    }

    @Override
    protected Void readInput(Void unused) {
        Terminal.prompt("Action", List.of(), List.of(
                new Option("check", "Check incoming monthly reports.", analyticsService::check),
                new Option("log", "Log incoming stock.", analyticsService::log),
                new Option("view", "Analytics summaries.", () -> {
                    new AnalyticsScreen(analyticsService);
                }),
                new Option("stats", "View and manage statistics.", () -> {
                    new TrendScreen();
                }),
                new Option("back", "Quit to main screen.", () -> running[0] = false)
        ));
        return null;
    }

    @Override
    protected Void nextScreen(Void current, Void input) {
        return null;
    }

    @Override
    protected boolean shouldExit(Void input) {
        return !running[0];
    }
}
