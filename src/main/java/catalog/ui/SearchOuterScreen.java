package catalog.ui;

import common.base.ScreenProgramTemplate;
import common.model.Product;

import java.util.List;

/**
 * Outer search session loop (query → results) using {@link ScreenProgramTemplate}.
 */
final class SearchOuterScreen extends ScreenProgramTemplate<Void, Void> {

    private final SearchScreen screen;
    private String query = "";
    private boolean stop;

    SearchOuterScreen(SearchScreen screen) {
        this.screen = screen;
    }

    @Override
    protected Void initialScreen() {
        return null;
    }

    @Override
    protected void render(Void unused) {
        // Search results UI is drawn inside handleResults
    }

    @Override
    protected Void readInput(Void unused) {
        List<Product> results = screen.getService().search(query);
        query = screen.handleResults(results, query);
        if (query == null) {
            stop = true;
        }
        return null;
    }

    @Override
    protected Void nextScreen(Void current, Void input) {
        return null;
    }

    @Override
    protected boolean shouldExit(Void input) {
        return stop;
    }
}
