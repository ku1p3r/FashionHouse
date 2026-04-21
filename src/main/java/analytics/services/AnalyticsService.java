package analytics.services;

import analytics.util.ReportReader;
import common.model.Product;
import common.model.Retailer;
import common.util.EntityLoader;
import common.util.Serializer;
import common.util.Terminal;
import common.wrapper.Option;
import common.wrapper.Period;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Service for managing and storing analytics.
 *
 * Author: Jase Beaubien
 */
public class AnalyticsService {
    private static final String[] RETAILER_HEADER = new String[]{"period","product","initial","stocked","sold"};

    /**
     * Terminal management for checking incoming reports and registering them to database.
     */
    public void check(){
        String[] input;
        while(true){
            input = Terminal.getInput("Period [month/year]").split("/");
            if(input.length == 2){
                break;
            }
            System.out.println(Terminal.RED + "Invalid input format. Expected 'month/year'." + Terminal.RESET);
        }
        int month = Integer.parseInt(input[0]);
        int year = Integer.parseInt(input[1]);

        // TODO get all files in retailers/reports that end in (month-year).report
        Path reportsDir = Paths.get("retailers", "reports");
        String suffix = "(" + month + "-" + year + ").report";
        List<Path> matchingReports = new ArrayList<>();

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(
                reportsDir,
                entry -> Files.isRegularFile(entry)
                        && entry.getFileName().toString().endsWith(suffix)
        )){
            for(Path reportPath : stream){
                matchingReports.add(reportPath);
            }
        }catch(IOException e) {
            System.out.println(Terminal.RED + "Failed to read reports: " + e.getMessage() + Terminal.RESET);
        }

        Path[] chosenReport = {null};
        List<Option> options = new ArrayList<>();
        for(Path path : matchingReports){
            String name = ReportReader.getName(path, suffix);
            options.add(new Option(name, "Open report.", () -> chosenReport[0] = path));
        }
        Terminal.prompt("Select Report", List.of(), options);

        HashMap<Product, Integer> report = ReportReader.read(chosenReport[0].toString());

        boolean[] running = {true};
        while(running[0]){
            Terminal.prompt("Report Usage", List.of(), List.of(
                    new Option("view", "Inspect report in terminal.", () -> {
                        for(int i = 0; i < report.size(); i++){
                            String product = (String)report.keySet().toArray()[i];
                            int quantity = report.get(product);
                            System.out.println(Terminal.GREEN + product + ": " + quantity + Terminal.RESET);
                        }
                    }),
                    new Option("back", "Return to analytics.", () -> running[0] = false)
            ));
        }
    }

    /**
     * Add report to retailer analytics file. If file doesn't exist, create new file.
     *
     * @param retailerPath
     * @param report
     * @param period
     */
    public void registerReport(String retailerPath, HashMap<Product, Integer> report, Period period){
        Serializer serializer;
        // If exists, set serializer to existing file, else create new file.
        try{
            serializer = new Serializer(retailerPath);
        }catch (Exception e){
            serializer = new Serializer(RETAILER_HEADER);
        }

        for(int i = 0; i < report.size(); i++){
            try {
                serializer.push(
                        period.getMonth() + "-" + period.getYear(),
                        (String)report.keySet().toArray()[i],
                        0,
                        0,
                        report.get(report.keySet().toArray()[i])
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        try {
            serializer.save(retailerPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Terminal management for logging stock to database.
     */
    public void log(){
        String[] input = {"0", "0"};
        while(true) {
            boolean[] runningC = {true};
            while (true) {
                String rawInput = Terminal.getInputWithExit("Select period", "month/year", runningC);
                if (!runningC[0] || rawInput == null) {
                    break;
                }
                input = rawInput.split("/");
                if(input.length == 2) {
                    break;
                }
                System.out.println(Terminal.RED + "Invalid input format. Expected 'month/year'." + Terminal.RESET);
            }
            if(!runningC[0]){
                break;
            }
            int month = Integer.parseInt(input[0]);
            int year = Integer.parseInt(input[1]);

            while (true) {
                boolean[] runningA = {true};
                String storeName = Terminal.getInputWithExit("Select store", "store", runningA);
                if (!runningA[0]) {
                    break;
                }

                while (true) {
                    boolean[] runningB = {true};
                    String productName = Terminal.getInputWithExit("Select for " + storeName, "product", runningB);
                    if (!runningB[0]) {
                        break;
                    }
                    int quantity = Integer.parseInt(Terminal.getInput("Added stock quantity for " + productName));

                    EntityLoader el = new EntityLoader();
                    stock(el.getRetailerByName(storeName), el.getProductByName(productName), new Period(month, year), quantity);
                }
            }
        }
    }

    /**
     * Add stock and update retailer analytics file.
     * Depreciated. Use store/*.catalog instead.
     *
     * @param retailer
     * @param product
     * @param period
     * @param quantity
     */
    public void stock(Retailer retailer, Product product, Period period, int quantity){
        Serializer serializer;
        String path = "retailers/" + retailer.getName() + ".retailer";
        try{
            serializer = new Serializer(path);
        }catch (Exception e){
            serializer = new Serializer(RETAILER_HEADER);
        }
        ArrayList<Integer> indices = serializer.getRows(List.of(
                new Serializer.Criterion("product", product.getName()),
                new Serializer.Criterion("period", period.getFileString())
        ));
        if(indices.isEmpty()){
            try {
                serializer.push(
                        period.getMonth() + "-" + period.getYear(),
                        product.getName(),
                        0,
                        quantity,
                        0
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            serializer.set("stocked", indices.get(0), quantity);
        }
        try {
            serializer.save(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
