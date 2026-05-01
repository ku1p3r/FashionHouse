import analytics.AnalyticsProgram;

final class AnalyticsDepartmentState implements DepartmentState {
    @Override
    public void open(String[] args) {
        AnalyticsProgram.main(args);
    }
}
