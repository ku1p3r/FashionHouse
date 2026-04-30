package production;

import catalog.service.CatalogService;
import catalog.ui.StoreSelectionScreen;
import common.base.ScreenProgramTemplate;
import common.util.Terminal;

/**
 * Production flow: store selection, then production console, using {@link ScreenProgramTemplate}.
 */
final class ProductionProgramScreen extends ScreenProgramTemplate<ProductionProgramScreen.FlowState, ProductionProgramScreen.FlowState> {

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
        // Store and production UIs draw themselves
    }

    @Override
    protected FlowState readInput(FlowState state) {
        if (state.step == 0) {
            StoreSelectionScreen storeScreen = new StoreSelectionScreen();
            state.service = storeScreen.run();
        } else if (state.step == 1) {
            ProductionSelectionScreen prodScreen = new ProductionSelectionScreen(state.service);
            prodScreen.run();
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
            state.step = 2;
        }
        return state;
    }

    @Override
    protected boolean shouldExit(FlowState state) {
        return state.step == 2;
    }
}
