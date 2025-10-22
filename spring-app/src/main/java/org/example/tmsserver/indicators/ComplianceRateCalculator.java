package org.example.tmsserver.indicators;

public class ComplianceRateCalculator implements IndicatorCalculator {

    @Override
    public String getIndicatorName() {
        return "Compliance Rate";
    }

    @Override
    public int mapValueToLevel(int value) {
        if (value <= 1) return 1;
        if (value <= 2) return 2;
        if (value <= 3) return 3;
        if (value <= 4) return 4;
        return 5;
    }
}