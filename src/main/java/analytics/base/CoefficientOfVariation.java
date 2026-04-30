package analytics.base;

import common.model.Product;
import common.model.Retailer;
import common.wrapper.Period;

import java.util.ArrayList;
import java.util.List;

public class CoefficientOfVariation implements Metric {
    private final Metric child;
    private final Period start;
    private final Period end;

    public CoefficientOfVariation(Metric child, Period start, Period end){
        this.child = child;
        this.start = start;
        this.end = end;
    }

    @Override
    public double evaluate(Retailer retailer, Iterable<Product> products, Period ignored){
        List<Integer> sales = new ArrayList<>();
        int month = start.getMonth();
        int year = start.getYear();
        while(year < end.getYear() || (year == end.getYear() && month <= end.getMonth())){
            Period current = new Period(month, year);
            sales.add(retailer.getSales(products, current));
            month++;
            if(month > 12){
                month = 1;
                year++;
            }
        }
        int size = sales.size();
        if(size == 0){
            System.out.println("No sales data available for the given period.");
            return 0f;
        }

        double mean = 0.0;
        for(Integer sale : sales){
            mean += (double)sale;
        }
        mean /= size;
        if(mean == 0.0){
            return 0f;
        }

        double sum = 0.0;
        for(Integer sale : sales){
            sum += Math.pow(sale - mean, 2);
        }
        double dev = Math.sqrt(sum / size);

        return dev / mean;
    }
}
