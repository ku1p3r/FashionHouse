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
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Retailer implements Selectable {
    private static int nextId = 0;
    private String name;
    private String id;
    private HashMap<Product, Integer> products = new HashMap<>();
    private HashMap<Period, SalesReport> salesReports = new HashMap<>();
    private Set<Product> productSet = new HashSet<>();

    /**
     * Constructor for Retailer.
     * @param name
     */
    public Retailer(String name){
        this.name = name;
        nextId++;
        this.id = Integer.toString(nextId);
        
        loadSalesReports();
    }

    /**
     * Loads sales reports.
     */
    private void loadSalesReports() {
        File reportsDir = new File("retailers/reports");
        if(!reportsDir.exists() || !reportsDir.isDirectory()){
            return;
        }

        Pattern pattern = Pattern.compile(Pattern.quote(name) + "\\((\\d+)-(\\d+)\\)\\.report");

        File[] reportFiles = reportsDir.listFiles();
        if(reportFiles == null){
            return;
        }

        for(File file : reportFiles){
            Matcher matcher = pattern.matcher(file.getName());
            if(matcher.matches()){
                int month = Integer.parseInt(matcher.group(1));
                int year = Integer.parseInt(matcher.group(2));
                Period period = new Period(month, year);

                HashMap<Product, Integer> sales = new HashMap<>();
                try(BufferedReader reader = new BufferedReader(new FileReader(file))){
                    String line;
                    boolean isHeader = true;
                    while((line = reader.readLine()) != null){
                        if (isHeader) {
                            isHeader = false;
                            continue;
                        }
                        String[] parts = line.split("\\|");
                        if(parts.length == 2){
                            String productKey = parts[0].trim();
                            int sold = Integer.parseInt(parts[1].trim());
                            Product product = new Product(productKey, productKey, "", 0, 0, "", "", "");
                            productSet.add(product);
                            sales.put(product, sold);
                        }
                    }
                }catch(IOException e){
                    System.err.println("Error reading report file: " + file.getName());
                }

                SalesReport report = new SalesReport(period, sales);
                salesReports.put(period, report);
            }
        }
    }

    /**
     * Gets the set of products that this retailer has sales data for.
     * @return productSet
     */
    public Set<Product> getProducts() {
        return productSet;
    }

    /**
     * Gets the total sales for the given products and period.
     * @param products
     * @param period
     * @return
     */
    public int getSales(Iterable<Product> products, Period period) {
        SalesReport report = salesReports.get(period);
        if (report == null) {
            return 0;
        }
        int totalSales = 0;
        for (Product product : products) {
            totalSales += getReportedSales(product, report);
        }
        return totalSales;
    }

    /**
     * Gets the total sales for the given product and period.
     * @param product
     * @param period
     * @return
     */
    public int getSales(Product product, Period period) {
        SalesReport report = salesReports.get(period);
        if (report == null) {
            return 0;
        }
        return getReportedSales(product, report);
    }

    /**
     * Helper method to get the sales for a single product from a report.
     * @param requestedProduct
     * @param report
     * @return
     */
    private int getReportedSales(Product requestedProduct, SalesReport report) {
        for(Map.Entry<Product, Integer> saleEntry : report.sales().entrySet()){
            if(productsMatch(requestedProduct, saleEntry.getKey())){
                return saleEntry.getValue();
            }
        }
        return 0;
    }

    /**
     * Helper method to determine if a requested product matches a product in the report.
     * @param requestedProduct
     * @param reportedProduct
     * @return
     */
    private boolean productsMatch(Product requestedProduct, Product reportedProduct) {
        if(requestedProduct.equals(reportedProduct)){
            return true;
        }

        String requestedId = requestedProduct.getId();
        String requestedName = requestedProduct.getName();
        String reportedId = reportedProduct.getId();
        String reportedName = reportedProduct.getName();

        return matchesValue(requestedId, reportedId)
                || matchesValue(requestedId, reportedName)
                || matchesValue(requestedName, reportedId)
                || matchesValue(requestedName, reportedName);
    }

    /**
     * Helper method to compare two strings for a match.
     * @param left
     * @param right
     * @return
     */
    private boolean matchesValue(String left, String right) {
        return left != null && left.equalsIgnoreCase(right);
    }

    /**
     * Gets the retailer's ID.
     * @return id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Gets the retailer's name.
     * @return name
     */
    @Override
    public String getName(){
        return name;
    }
}
