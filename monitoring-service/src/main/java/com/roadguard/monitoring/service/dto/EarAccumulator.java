package com.roadguard.monitoring.service.dto;

public class EarAccumulator {
    double sum;
    long count;

    public synchronized void add(double value) {
        sum += value;
        count++;
    }

    public synchronized double averageAndReset() {
        if (count == 0) return Double.NaN;
        double avg = sum / count;
        sum = 0;
        count = 0;
        return avg;
    }
}
