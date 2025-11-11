package org.example.tmsserver.config;

import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RegionWeatherConfig {

    // Coordenadas fixas por região [latitude, longitude]
    private static final Map<Integer, double[]> REGION_COORDINATES = Map.of(
        1, new double[]{-23.2177, -45.8893},  //Zona Sul
        2, new double[]{-23.2151, -45.8804},      // Zona Sudeteste
        3, new double[]{-23.2060, -45.8274},      // Zona Leste
        4, new double[]{-23.2038, -45.8973},      // Zona Central
        5, new double[]{-23.2170, -45.9078},      // Zona Oeste
        6, new double[]{-23.1759, -45.8884}       // Zona Norte
    );


    public static double[] getCoordinates(Integer regionId) {
        double[] coords = REGION_COORDINATES.getOrDefault(regionId, new double[]{0.0, 0.0});
        if (coords[0] == 0.0 && coords[1] == 0.0) {
            System.err.println("Coordenadas não configuradas para regionId=" + regionId);
        }
        return coords;
    }

    public static boolean hasCoordinates(Integer regionId) {
        double[] coords = REGION_COORDINATES.get(regionId);
        return coords != null && (coords[0] != 0.0 || coords[1] != 0.0);
    }
}

