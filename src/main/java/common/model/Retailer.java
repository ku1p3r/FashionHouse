package common.model;

import common.base.Selectable;
import common.wrapper.Period;
import common.wrapper.SalesReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Retailer implements Selectable {
    // TODO add other attributes
    private static int nextId = 0;
    private String name;
    private String id;
    private HashMap<Product, Integer> products = new HashMap<>();
    private HashMap<Period, SalesReport> salesReports = new HashMap<>();
    private Set<Product> productSet = new HashSet<>();

    public Retailer(String name){
        this.name = name;
        nextId++;
        this.id = Integer.toString(nextId);
        
        loadSalesReports();
    }

    private void loadSalesReports() {
        File reportsDir = new File("retailers/reports");
        if (!reportsDir.exists() || !reportsDir.isDirectory()) {
            return;
        }

        // Pattern to match files like: retailerName(month-year).report
        Pattern pattern = Pattern.compile(Pattern.quote(name) + "\\((\\d+)-(\\d+)\\)\\.report");

        File[] reportFiles = reportsDir.listFiles();
        if (reportFiles == null) {
            return;
        }

        for (File file : reportFiles) {
            Matcher matcher = pattern.matcher(file.getName());
            if (matcher.matches()) {
                int month = Integer.parseInt(matcher.group(1));
                int year = Integer.parseInt(matcher.group(2));
                Period period = new Period(month, year);

                HashMap<Product, Integer> sales = new HashMap<>();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    boolean isHeader = true;
                    while ((line = reader.readLine()) != null) {
                        if (isHeader) {
                            isHeader = false;
                            continue;
                        }
                        String[] parts = line.split("\\|");
                        if (parts.length == 2) {
                            String productId = parts[0].trim();
                            int sold = Integer.parseInt(parts[1].trim());
                            // Create a minimal Product with the id from the report
                            Product product = new Product(productId, productId, "", 0, 0, "", "");
                            productSet.add(product);
                            sales.put(product, sold);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading report file: " + file.getName());
                }

                SalesReport report = new SalesReport(period, sales);
                salesReports.put(period, report);
            }
        }
    }

    public Set<Product> getProducts() {
        return productSet;
    }

    public int getSales(Iterable<Product> products, Period period) {
        SalesReport report = salesReports.get(period);
        if (report == null) {
            return 0;
        }
        int totalSales = 0;
        for (Product product : products) {
            totalSales += report.sales().getOrDefault(product, 0);
        }
        return totalSales;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName(){
        return name;
    }
}
