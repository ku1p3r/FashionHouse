package analytics.ui;

import analytics.services.AnalyticsService;
import common.model.Product;
import common.util.EntityLoader;
import common.util.Serializer;
import common.util.Terminal;
import common.wrapper.Period;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnalyticsScreen {
    /**
     * Displays analytics summaries based on retailer reports within a specified period.
     *
     * @param service
     */
    public AnalyticsScreen(AnalyticsService service) {
        new AnalyticsSummaryScreen().run();
    }

    public HashMap<Product, Integer> getProductSales(Period start, Period end) {
        return computeProductSales(start, end);
    }

    static HashMap<Product, Integer> computeProductSales(Period start, Period end) {
        HashMap<Product, Integer> productSales = new HashMap<>();

        for (int year = start.getYear(); year <= end.getYear(); year++) {
            for (int month = (year == start.getYear() ? start.getMonth() : 1);
                 month <= (year == end.getYear() ? end.getMonth() : 12);
                 month++) {
                ArrayList<Path> retailerPaths = new ArrayList<>();
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(
                        Path.of("retailers"),
                        entry -> Files.isRegularFile(entry)
                                && entry.getFileName().toString().endsWith(".retailer")
                )) {
                    for (Path reportPath : stream) {
                        retailerPaths.add(reportPath);
                    }
                } catch (IOException e) {
                    System.out.println(Terminal.RED + "Failed to read reports: " + e.getMessage() + Terminal.RESET);
                }

                for (Path retailerPath : retailerPaths) {
                    Serializer serializer = new Serializer(retailerPath.toString());
                    ArrayList<Integer> indices = serializer.getRows(List.of(
                            new Serializer.Criterion("period", month + "-" + year)
                    ));

                    EntityLoader pl = new EntityLoader();
                    for (int index : indices) {
                        String productName = serializer.get("product", index, String.class);
                        int quantity = serializer.get("sold", index, Integer.class);
                        productSales.put(pl.getProductByName(productName),
                                productSales.getOrDefault(pl.getProductByName(productName), 0) + quantity);
                    }
                }
            }
        }

        return productSales;
    }
}
