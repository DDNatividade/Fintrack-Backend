package com.apis.fintrack.domain.analysis.model.metric;

/**
 * Marker interface for analysis metrics.
 * Represents the result of a financial analysis.
 * Implementations define the semantic shape of the metric
 * (scalar, time series, distribution, etc.).
 */
public sealed interface AnalysisMetric
        permits ScalarMetric, SeriesMetric, DistributionMetric {
}
