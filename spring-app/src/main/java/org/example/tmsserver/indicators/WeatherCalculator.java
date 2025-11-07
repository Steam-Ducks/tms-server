package org.example.tmsserver.indicators;

public class WeatherCalculator implements IndicatorCalculator {

    @Override
    public String getIndicatorName() {
        return "Weather";
    }

    @Override
    public int mapValueToLevel(int weatherCode) {


        return switch (weatherCode) {
            // Clear sky - optimal conditions
            case 0 -> 1;

            // Partly cloudy, mainly clear - good conditions
            case 1, 2, 3 -> 2;

            // Fog - reduced visibility
            case 45, 46, 47, 48 -> 3;

            // Light drizzle/rain - mild impact
            case 51, 53, 55, 56, 57, 61 -> 3;

            // Moderate rain - significant impact
            case 63, 80 -> 4;

            // Heavy rain, freezing rain, violent showers - severe impact
            case 65, 67, 81, 82 -> 5;

            // Thunderstorms - critical impact
            case 95, 96, 99 -> 5;

            // Default for any other weather conditions
            default -> 3;
        };
    }
}
