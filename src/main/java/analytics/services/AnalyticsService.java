package analytics.services;

import analytics.AnalyticsProgram;
import analytics.util.ReportReader;
import common.model.Product;
import common.model.Retailer;
import common.util.Serializer;
import common.util.Terminal;
import common.wrapper.Option;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnalyticsService {
    private static final String[] RETAILER_HEADER = new String[]{"period","product","initial","stocked","sold"};

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

        HashMap<String, Integer> report = ReportReader.read(chosenReport[0].toString());

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
                    new Option("store", "Register to database.", () -> {
                        Serializer serializer;
                        String name = ReportReader.getName(chosenReport[0], suffix);
                        String retailerPath = "retailers/" + name + ".retailer";
                        // TODO if exists, set serializer to existing file, else create new file
                        try{
                            serializer = new Serializer(retailerPath);
                        }catch (Exception e){
                            serializer = new Serializer(RETAILER_HEADER);
                        }

                        for(int i = 0; i < report.size(); i++){
                            try {
                                serializer.push(
                                        month + "-" + year,
                                        (String)report.keySet().toArray()[i],
                                        0,
                                        0,
                                        report.get((String)report.keySet().toArray()[i])
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
                    }),
                    new Option("back", "Return to analytics.", () -> running[0] = false)
            ));
        }
    }

    public void log(){
        // TODO
    }

    public void reportSale(Retailer retailer, Product product){
    }

    public void getMonthlySales(Retailer retailer){
    }
}
