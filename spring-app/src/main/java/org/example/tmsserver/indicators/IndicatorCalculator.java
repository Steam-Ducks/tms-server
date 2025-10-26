package org.example.tmsserver.indicators;

public interface IndicatorCalculator {
    String getIndicatorName();
    int mapValueToLevel(int value);
}