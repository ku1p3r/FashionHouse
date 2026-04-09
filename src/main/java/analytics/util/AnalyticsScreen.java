package analytics.util;

import analytics.services.AnalyticsService;
import common.util.Serializer;
import common.util.Terminal;

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
        String[] input;

        while(true){
            input = Terminal.getInput("From period [month/year]").split("/");
            if(input.length == 2){
                break;
            }
            System.out.println(Terminal.RED + "Invalid input format. Expected 'month/year'." + Terminal.RESET);
        }
        int startMonth = Integer.parseInt(input[0]);
        int startYear = Integer.parseInt(input[1]);

        while(true){
            input = Terminal.getInput("To period [month/year]").split("/");
            if(input.length == 2){
                break;
            }
            System.out.println(Terminal.RED + "Invalid input format. Expected 'month/year'." + Terminal.RESET);
        }
        int endMonth = Integer.parseInt(input[0]);
        int endYear = Integer.parseInt(input[1]);

        HashMap<String, Integer> productSales = new HashMap<>();

        for(int year = startYear; year <= endYear; year++){
            for(int month = (year == startYear ? startMonth : 1); month <= (year == endYear ? endMonth : 12); month++){
                ArrayList<Path> retailerPaths = new ArrayList<>();
                try(DirectoryStream<Path> stream = Files.newDirectoryStream(
                        Path.of("retailers"),
                        entry -> Files.isRegularFile(entry)
                                && entry.getFileName().toString().endsWith(".retailer")
                )){
                    for(Path reportPath : stream){
                        retailerPaths.add(reportPath);
                    }
                }catch(IOException e) {
                    System.out.println(Terminal.RED + "Failed to read reports: " + e.getMessage() + Terminal.RESET);
                }

                for(Path retailerPath : retailerPaths){
                    Serializer serializer = new Serializer(retailerPath.toString());
                    ArrayList<Integer> indices = serializer.getRows(List.of(
                            new Serializer.Criterion("period", month + "-" + year)
                    ));

                    for(int index : indices){
                        String product = serializer.get("product", index, String.class);
                        int quantity = serializer.get("sold", index, Integer.class);
                        productSales.put(product, productSales.getOrDefault(product, 0) + quantity);
                    }
                }
            }
        }

        for(String product : productSales.keySet()){
            System.out.println(Terminal.MAGENTA + product + Terminal.RESET + ": " + productSales.get(product));
        }
    }
}
