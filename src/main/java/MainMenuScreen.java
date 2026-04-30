
import advertising.AdvertisingMain;
import analytics.AnalyticsProgram;
import catalog.CatalogProgram;
import common.base.ScreenProgramTemplate;
import common.util.Terminal;
import common.wrapper.Option;
import hr.HumanResourcesProgram;
import production.ProductionProgram;
import sales.SalesSystem;
import security.SecurityProgram;

import java.util.List;

/**
 * Root Fashion House menu using {@link ScreenProgramTemplate}.
 */
final class MainMenuScreen extends ScreenProgramTemplate<Void, Void> {

    private final String[] args;
    private final boolean[] running = {true};

    MainMenuScreen(String[] args) {
        this.args = args;
    }

    @Override
    protected Void initialScreen() {
        return null;
    }

    @Override
    protected void render(Void unused) {
        Terminal.clearScreen();
        Terminal.printHeader("Fashion House");
    }

    @Override
    protected Void readInput(Void unused) {
        Option catalog = new Option("1", "Product Catalog and Inventory", () -> CatalogProgram.main(args));
        Option analytics = new Option("2", "DBMS and Analytics", () -> AnalyticsProgram.main(args));
        Option sales = new Option("3", "Sales & Retailer Console", () -> SalesSystem.main(args));
        Option production = new Option("4", "Production Management", () -> ProductionProgram.main(args));
        Option security = new Option("5", "Security", () -> SecurityProgram.main(args));
        Option hr = new Option("6", "Human Resources Console", () -> HumanResourcesProgram.main(args));
        Option advertising = new Option("7", "Advertising", () -> AdvertisingMain.main(args));
        Option exit = new Option("quit", "Exit program", () -> running[0] = false);

        Terminal.prompt("Select Program", List.of(), List.of(
                catalog,
                analytics,
                sales,
                production,
                security,
                hr,
                advertising,
                exit
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
