package org.example.tmsserver.indicators;

public class AverageSpeedCalculator implements IndicatorCalculator {

    @Override
    public String getIndicatorName() {
        return "Average Speed";
    }

    @Override
    public int mapValueToLevel(int value) {
        if (value >= 76) return 1;
        if (value >= 74) return 2;
        if (value >= 72) return 3;
        if (value >= 70) return 4;
        return 5;
    }
}
