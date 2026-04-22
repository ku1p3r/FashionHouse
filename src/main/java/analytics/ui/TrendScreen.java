package analytics.ui;

import analytics.services.TrendService;
import analytics.wrappers.TrendResult;
import common.model.Product;
import common.model.Retailer;
import common.util.EntityLoader;
import common.util.Terminal;
import common.wrapper.Option;
import common.wrapper.Period;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrendScreen {

    private final EntityLoader el = new EntityLoader();

    public TrendScreen() {
        boolean[] running = {true};
        while(running[0]){
            Terminal.prompt("Statistics", List.of(), List.of(
                    new Option("trend", "Observe trends.", () -> {
                        Retailer retailer = promptRetailer();
                        Product product = promptProduct();
                        Period start = Terminal.promptPeriod("Start date");
                        Period end = Terminal.promptPeriod("End date");
                        TrendResult result = TrendService.getTrend(
                                "trend",
                                List.of(retailer),
                                List.of(product),
                                start,
                                end
                        );
                        Terminal.println(result.direction() + " by " + result.percentageChange() + "%");
                    }),
                    new Option("mean", "Observe mean sales of the past year.", () -> {
                        Retailer retailer = promptRetailer();
                        Period period = Terminal.promptPeriod("Current date");
                        HashMap<Product, Integer> result = TrendService.getMeanAnnualSales(retailer, period);
                        for(Map.Entry<Product, Integer> entry : result.entrySet()){
                            System.out.println(entry.getKey().getName() + ": " + entry.getValue());
                        }
                        //Terminal.println("Mean: " + result);
                    }),
                    new Option("estimate", "Anticipate.", () -> {
                        Retailer retailer = promptRetailer();
                        Period period = Terminal.promptPeriod("Next period");
                        HashMap<Product, Integer> result = TrendService.anticipateStock(retailer, period);
                        for(Product product : result.keySet()){
                            Terminal.println(product.getName() + ": " + result.get(product));
                        }
                    }),
                    new Option("volatile", "Observe volatility.", () -> {
                        Retailer retailer = promptRetailer();
                        Product product = promptProduct();
                        Period start = Terminal.promptPeriod("Start date");
                        Period end = Terminal.promptPeriod("End date");
                        float result = TrendService.getSalesVolatility(retailer, List.of(product), start, end);
                        Terminal.println("Volatility: " + result);
                    }),
                    new Option("top", "Observe top.", () -> {
                        Retailer retailer = promptRetailer();
                        Period period = Terminal.promptPeriod("Period");
                        int n = Terminal.promptInt("How many top products? ");
                        int i = 1;
                        for(Product product : TrendService.getTopProducts(retailer, period, n)){
                            Terminal.println("(" + (i++) + ") " + product.getName());
                        }
                    }),
                    new Option("season", "Observe seasonal indexes.", () -> {
                        Retailer retailer = promptRetailer();
                        Product product = promptProduct();
                        int startYear = Terminal.promptInt("Start year");
                        int endYear = Terminal.promptInt("End year");
                        HashMap<Integer, Float> result = TrendService.getSeasonalIndexes(
                                retailer, List.of(product), startYear, endYear);
                        for(int month = 1; month <= 12; month++){
                            Terminal.println("Month " + month + ": " + result.get(month));
                        }
                    }),
                    new Option("back", "Quit to previous menu.", () -> running[0] = false)
            ));
        }
    }

    private Retailer promptRetailer() {
        return Terminal.prompt("Select retailer", el.getAllRetailers(), List.of());
    }

    private Product promptProduct() {
        return Terminal.prompt("Select product", el.getAllProducts(), List.of());
    }
}
