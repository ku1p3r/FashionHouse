package catalog;

import catalog.service.CatalogService;
import catalog.ui.SearchScreen;
import catalog.ui.StoreSelectionScreen;
import common.base.ScreenProgramTemplate;
import common.util.Terminal;

/**
 * Catalog flow: store selection, then search session, using {@link ScreenProgramTemplate}.
 */
final class CatalogProgramScreen extends ScreenProgramTemplate<CatalogProgramScreen.FlowState, CatalogProgramScreen.FlowState> {

    static final class FlowState {
        int step;
        CatalogService service;
    }

    @Override
    protected FlowState initialScreen() {
        FlowState s = new FlowState();
        s.step = 0;
        return s;
    }

    @Override
    protected void render(FlowState state) {
        // Store and search UIs draw themselves
    }

    @Override
    protected FlowState readInput(FlowState state) {
        if (state.step == 0) {
            StoreSelectionScreen storeScreen = new StoreSelectionScreen();
            state.service = storeScreen.run();
        } else if (state.step == 1) {
            SearchScreen searchScreen = new SearchScreen(state.service);
            searchScreen.run();
        }
        return state;
    }

    @Override
    protected FlowState nextScreen(FlowState state, FlowState input) {
        if (state.step == 0) {
            if (state.service == null) {
                Terminal.clearScreen();
                Terminal.println(Terminal.CYAN + "Goodbye." + Terminal.RESET);
                Terminal.println();
                state.step = 2;
            } else {
                state.step = 1;
            }
        } else if (state.step == 1) {
            Terminal.clearScreen();
            Terminal.println(Terminal.CYAN + "Session closed. Catalog saved to: "
                    + state.service.getFilePath() + Terminal.RESET);
            Terminal.println();
            state.step = 2;
        }
        return state;
    }

    @Override
    protected boolean shouldExit(FlowState state) {
        return state.step == 2;
    }
}
