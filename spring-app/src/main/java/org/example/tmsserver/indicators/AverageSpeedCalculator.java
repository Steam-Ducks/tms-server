package org.example.tmsserver.indicators;

public class AverageSpeedCalculator implements IndicatorCalculator {

    @Override
    public String getIndicatorName() {
        return "Average Speed";
    }

    @Override
    public int mapValueToLevel(int value) {
        if (value >= 75) return 1;
        if (value >= 73) return 2;
        if (value >= 70) return 3;
        if (value >= 68) return 4;
        return 5;
    }
}
