package analytics.ui;

import analytics.services.TrendService;
import analytics.wrappers.TrendResult;
import common.model.Product;
import common.model.Retailer;
import common.util.EntityLoader;
import common.util.Terminal;
import common.wrapper.Option;
import common.wrapper.Period;

import java.util.HashMap;
import java.util.List;

public class TrendScreen {
    public TrendScreen() {
        boolean[] running = {true};
        while(running[0]){
            Terminal.prompt("Statistics", List.of(), List.of(
                    new Option("trend", "Observe trends.", () -> {
                        EntityLoader el = new EntityLoader();
                        TrendResult result = TrendService.getTrend("test", el.getAllRetailers(), el.getAllProducts(), new Period(1, 2023), new Period(6, 2029));
                        System.out.println(result.direction() + " by " + result.percentageChange() + "%");
                    }),
                    new Option("mean", "Observe mean.", () -> {
                        EntityLoader el = new EntityLoader();
                        float result = TrendService.getTotalMeanAnnualSales(el.getAllRetailers().iterator().next(), new Period(1, 2027));
                        System.out.println("Mean: " + result);
                    }),
                    new Option("estimate", "Anticipate.", () -> {
                        EntityLoader el = new EntityLoader();
                        HashMap<Product, Integer> result = TrendService.anticipateStock(el.getAllRetailers().iterator().next(), new Period(1, 2027));
                        for(Product product : result.keySet()){
                            System.out.println(product.getName() + ": " + result.get(product));
                        }
                    }),
                    new Option("volatile", "Observe volatility.", () -> {
                        EntityLoader el = new EntityLoader();
                        Retailer retailer = el.getAllRetailers().iterator().next();
                        float result = TrendService.getSalesVolatility(retailer, retailer.getProducts(), new Period(1, 2023), new Period(6, 2029));
                        System.out.println("Volatility: " + result);
                    }),
                    new Option("top", "Observe top.", () -> {
                        EntityLoader el = new EntityLoader();
                        int i = 1;
                        for(Product product : TrendService.getTopProducts(el.getAllRetailers().iterator().next(), new Period(1, 2027), 3)){
                            System.out.println("(" + (i++) + ") " + product.getName());
                        }
                    }),
                    new Option("season", "Observe seasonal indexes.", () -> {
                        EntityLoader el = new EntityLoader();
                        Retailer retailer = el.getAllRetailers().iterator().next();
                        HashMap<Integer, Float> result = TrendService.getSeasonalIndexes(retailer, retailer.getProducts(), 2023, 2029);
                        for (int month = 1; month <= 12; month++) {
                            System.out.printf("Month %2d: %.4f%n", month, result.get(month));
                        }
                    }),
                    new Option("back", "Quit to previous menu.", () -> running[0] = false)
            ));
        }
    }
}
