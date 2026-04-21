package analytics.wrappers;

public record TrendResult(
    String metricName,
    TrendDirection direction,
    double percentageChange
) {
}
