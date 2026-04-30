package analytics;

import analytics.services.AnalyticsService;

public class AnalyticsProgram {
    public static void main(String args[]){
        new AnalyticsMenuScreen(new AnalyticsService()).run();
    }
}
