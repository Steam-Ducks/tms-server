package org.example.tmsserver.indicators;

public class TrafficDensityCalculator implements IndicatorCalculator {

    @Override
    public String getIndicatorName() {
        return "Traffic Density";
    }

    @Override
    public int mapValueToLevel(int value) {
        if (value <= 2) return 1;
        if (value <= 5) return 2;
        if (value <= 7) return 3;
        if (value <= 10) return 4;
        return 5;
    }
}
