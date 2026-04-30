package catalog.ui;

import common.base.ScreenProgramTemplate;
import common.model.Product;
import common.util.Terminal;

/**
 * Product detail action menu using {@link ScreenProgramTemplate}.
 */
final class DetailMenuScreen extends ScreenProgramTemplate<Product, String> {

    private final DetailScreen screen;
    private final Product product;
    private DetailScreen.Result exitResult;
    private boolean done;

    DetailMenuScreen(DetailScreen screen, Product product) {
        this.screen = screen;
        this.product = product;
    }

    DetailScreen.Result runForResult() {
        run();
        return exitResult;
    }

    @Override
    protected Product initialScreen() {
        return product;
    }

    @Override
    protected void render(Product p) {
        screen.renderProductDetail(p);
    }

    @Override
    protected String readInput(Product p) {
        return Terminal.prompt("Action >");
    }

    @Override
    protected Product nextScreen(Product p, String choice) {
        DetailScreen.Result r = screen.handleDetailMenuChoice(p, choice);
        if (r != null) {
            exitResult = r;
            done = true;
        }
        return p;
    }

    @Override
    protected boolean shouldExit(String input) {
        return done;
    }
}
