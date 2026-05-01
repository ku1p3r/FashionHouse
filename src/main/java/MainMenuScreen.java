
import common.base.ScreenProgramTemplate;
import common.util.Terminal;
import common.wrapper.Option;
import java.util.List;

/**
 * Root Fashion House menu using {@link ScreenProgramTemplate}.
 */
final class MainMenuScreen extends ScreenProgramTemplate<Void, Void> {

    private final String[] args;
    private final boolean[] running = {true};
    private final DepartmentSwitchContext departmentSwitchContext;

    MainMenuScreen(String[] args) {
        this.args = args;
        this.departmentSwitchContext = new DepartmentSwitchContext(args);
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
        Option catalog = new Option("1", "Product Catalog and Inventory",
                () -> switchDepartment(new CatalogDepartmentState()));
        Option analytics = new Option("2", "DBMS and Analytics",
                () -> switchDepartment(new AnalyticsDepartmentState()));
        Option sales = new Option("3", "Sales & Retailer Console",
                () -> switchDepartment(new SalesDepartmentState()));
        Option production = new Option("4", "Production Management",
                () -> switchDepartment(new ProductionDepartmentState()));
        Option security = new Option("5", "Security",
                () -> switchDepartment(new SecurityDepartmentState()));
        Option hr = new Option("6", "Human Resources Console",
                () -> switchDepartment(new HumanResourcesDepartmentState()));
        Option advertising = new Option("7", "Advertising",
                () -> switchDepartment(new AdvertisingDepartmentState()));
        Option imageConsulting = new Option("8", "Image Consulting",
                () -> switchDepartment(new ImageConsultingDepartmentState()));
        Option exit = new Option("quit", "Exit program",
                () -> switchDepartment(new ExitDepartmentState(running)));

        Terminal.prompt("Select Program", List.of(), List.of(
                catalog,
                analytics,
                sales,
                production,
                security,
                hr,
                advertising,
                imageConsulting,
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

    private void switchDepartment(DepartmentState state) {
        departmentSwitchContext.setState(state);
        departmentSwitchContext.switchDepartment();
    }
}
