package analytics.services;

import analytics.util.ReportReader;
import analytics.wrappers.TrendDirection;
import analytics.wrappers.TrendResult;
import common.model.Product;
import common.model.Retailer;
import common.util.EntityLoader;
import common.util.Serializer;
import common.util.Terminal;
import common.wrapper.Option;
import common.wrapper.Period;

import java.io.File;
import java.util.*;

public class TrendService {
    public TrendService() {
        boolean[] running = {true};
        while(running[0]){
            Terminal.prompt("Report Usage", List.of(), List.of(
                    new Option("trend", "Observe trends.", () -> {
                        EntityLoader el = new EntityLoader();
                        TrendResult result = getTrend("test", el.getAllRetailers(), el.getAllProducts(), new Period(1, 2023), new Period(6, 2029));
                        System.out.println(result.direction() + " by " + result.percentageChange() + "%");
                    }),
                    new Option("estimate", "Anticipate.", () -> {
                        EntityLoader el = new EntityLoader();
                        HashMap<Product, Integer> result = anticipateStock(el.getAllRetailers().iterator().next(), new Period(1, 2027));
                        for(Product product : result.keySet()){
                            System.out.println(product.getName() + ": " + result.get(product));
                        }
                    }),
                    new Option("back", "Quit to previous menu.", () -> running[0] = false)
            ));
        }
    }
    
    /*public static int getSales(Iterable<Retailer> retailers, Iterable<Product> products, Period start, Period end){
        // Build set of valid product names
        Set<String> productNames = new HashSet<>();
        for (Product p : products) {
            productNames.add(p.getName());
        }

        // Build ordered list of periods from start to end
        List<Period> periods = new ArrayList<>();
        int month = start.getMonth();
        int year = start.getYear();
        while (year < end.getYear() || (year == end.getYear() && month <= end.getMonth())) {
            periods.add(new Period(month, year));
            month++;
            if (month > 12) {
                month = 1;
                year++;
            }
        }

        // Collect total sales across all periods and retailers, filtered by products
        int total = 0;
        for (Period period : periods) {
            for (Retailer retailer : retailers) {
                String path = "retailers/reports/" + retailer.getName() + "(" + period.getFileString() + ").report";
                File file = new File(path);
                if (!file.exists()) continue;
                HashMap<String, Integer> report = ReportReader.read(path);
                for (Map.Entry<String, Integer> entry : report.entrySet()) {
                    if (productNames.contains(entry.getKey())) {
                        total += entry.getValue();
                    }
                }
            }
        }
        return total;
    }*/

    public static TrendResult getTrend(String metricName, Iterable<Retailer> retailers, Iterable<Product> products, Period start, Period end) {
        List<Period> periods = new ArrayList<>();
        int month = start.getMonth();
        int year = start.getYear();
        while (year < end.getYear() || (year == end.getYear() && month <= end.getMonth())) {
            periods.add(new Period(month, year));
            month++;
            if (month > 12) {
                month = 1;
                year++;
            }
        }

        if (periods.isEmpty()) {
            return new TrendResult(metricName, TrendDirection.STABLE, 0.0);
        }

        List<Integer> periodTotals = new ArrayList<>();
        for (Period period : periods) {
            int total = 0;
            for(Retailer retailer : retailers){
                total += retailer.getSales(products, period);
            }
            periodTotals.add(total);
        }

        List<Integer> nonZeroPeriods = new ArrayList<>();
        for (Integer total : periodTotals) {
            if (total > 0) {
                nonZeroPeriods.add(total);
            }
        }

        if (nonZeroPeriods.isEmpty()) {
            return new TrendResult(metricName, TrendDirection.STABLE, 0.0);
        }

        double firstPeriod = nonZeroPeriods.get(0);
        double lastPeriod = nonZeroPeriods.get(nonZeroPeriods.size() - 1);
        double percentageChange = ((lastPeriod - firstPeriod) / firstPeriod) * 100;
        percentageChange = Math.round(percentageChange * 100.0) / 100.0;

        TrendDirection direction;
        if (percentageChange > 0) direction = TrendDirection.RISING;
        else if (percentageChange < 0) direction = TrendDirection.FALLING;
        else direction = TrendDirection.STABLE;

        return new TrendResult(metricName, direction, percentageChange);
    }

    public static HashMap<Product, Integer> getMeanAnnualSales(Retailer retailer, Period period) {
        HashMap<Product, Integer> result = new HashMap<>();
        int month = period.getMonth();
        int year = period.getYear() - 1;
        int sum = 0;
        for(int i = 0; i < 12; i++){
            month += 1;
            if(month > 12){
                month = 1;
                year += 1;
            }
            Period current = new Period(month, year);

            for(Product product : retailer.getProducts()){
                int num = retailer.getSales(List.of(product), current);

                if(result.containsKey(product)){
                    result.replace(product, result.get(product) + num);
                }else{
                    result.put(product, num);
                }
            }
        }

        for(Product product : result.keySet()){
            result.replace(product, (int)Math.ceil(result.get(product) / 12.0f));
        }

        return result;
    }

    public static HashMap<Product, Integer> anticipateStock(Retailer retailer, Period period) {
        HashMap<Product, Integer> result = new HashMap<>();

        HashMap<Product, Integer> MAS = getMeanAnnualSales(retailer, new Period(period.getMonth() - 1, period.getYear()));
        HashMap<Product, Integer> lastMAS = getMeanAnnualSales(retailer, new Period(period.getMonth() - 1, period.getYear() - 1));

        // sales(month, year - 1) * MAS/lastMAS
        for(Product product : retailer.getProducts()){
            int sales = retailer.getSales(List.of(product), new Period(period.getMonth(), period.getYear() - 1));
            int mas = MAS.getOrDefault(product, 0);
            int lastMas = lastMAS.getOrDefault(product, 0);
            int anticipated = (int)Math.ceil(sales * ((double)mas / (lastMas == 0 ? 1 : lastMas)));
            result.put(product, anticipated);
        }

        return result;
    }
}
