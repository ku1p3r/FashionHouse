package analytics.ui;

import common.base.ScreenProgramTemplate;
import common.model.Product;
import common.util.Terminal;
import common.wrapper.Period;

import java.util.HashMap;

/**
 * Period selection + sales summary display using {@link ScreenProgramTemplate}.
 */
final class AnalyticsSummaryScreen extends ScreenProgramTemplate<Void, Void> {

    private boolean finished;

    @Override
    protected Void initialScreen() {
        return null;
    }

    @Override
    protected void render(Void unused) {
        // Prompts and output happen in readInput
    }

    @Override
    protected Void readInput(Void unused) {
        String[] input;

        while (true) {
            input = Terminal.getInput("From period [month/year]").split("/");
            if (input.length == 2) {
                break;
            }
            System.out.println(Terminal.RED + "Invalid input format. Expected 'month/year'." + Terminal.RESET);
        }
        int startMonth = Integer.parseInt(input[0]);
        int startYear = Integer.parseInt(input[1]);

        while (true) {
            input = Terminal.getInput("To period [month/year]").split("/");
            if (input.length == 2) {
                break;
            }
            System.out.println(Terminal.RED + "Invalid input format. Expected 'month/year'." + Terminal.RESET);
        }

        int endMonth = Integer.parseInt(input[0]);
        int endYear = Integer.parseInt(input[1]);

        HashMap<Product, Integer> productSales = AnalyticsScreen.computeProductSales(
                new Period(startMonth, startYear),
                new Period(endMonth, endYear));

        for (Product product : productSales.keySet()) {
            System.out.println(Terminal.MAGENTA + product.getName() + Terminal.RESET + ": " + productSales.get(product));
        }

        finished = true;
        return null;
    }

    @Override
    protected Void nextScreen(Void current, Void input) {
        return null;
    }

    @Override
    protected boolean shouldExit(Void input) {
        return finished;
    }
}
