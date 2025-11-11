package org.example.tmsserver.service;

import org.springframework.stereotype.Component;

@Component
public class WeatherCodeMapper {

    /**
     * Mapeia weather code WMO para nível de severidade (1-5).
     *
     * Códigos WMO Weather Interpretation:
     * 0-1: Céu limpo/parcialmente nublado → Nível 1 (ótimo)
     * 2-3: Nublado → Nível 2 (bom)
     * 45-48: Nevoeiro → Nível 3 (regular)
     * 51-67: Chuva leve a moderada → Nível 3 (regular)
     * 71-77: Neve leve a moderada → Nível 4 (ruim)
     * 80-82: Chuva forte → Nível 4 (ruim)
     * 85-86: Neve forte → Nível 5 (crítico)
     * 95-99: Tempestades → Nível 5 (crítico)
     *
     * @param weatherCode código WMO retornado pela API
     * @return nível de 1 (ótimo) a 5 (crítico)
     */
    public int mapWeatherCodeToLevel(int weatherCode) {
        System.out.println("Mapeando weatherCode=" + weatherCode);

        // Nível 1: Céu limpo ou quase limpo
        if (weatherCode <= 1) {
            return 1;
        }

        // Nível 2: Nublado
        if (weatherCode <= 3) {
            return 2;
        }

        // Nível 3: Nevoeiro ou chuva leve/moderada
        if (weatherCode >= 45 && weatherCode <= 48) {
            return 3;
        }
        if (weatherCode >= 51 && weatherCode <= 67) {
            return 3;
        }

        // Nível 4: Neve leve/moderada ou chuva forte
        if (weatherCode >= 71 && weatherCode <= 77) {
            return 4;
        }
        if (weatherCode >= 80 && weatherCode <= 82) {
            return 4;
        }

        // Nível 5: Condições severas (neve forte, tempestades)
        if (weatherCode >= 85 && weatherCode <= 86) {
            return 5;
        }
        if (weatherCode >= 95 && weatherCode <= 99) {
            return 5;
        }

        // Códigos não mapeados: retorna nível 3 (regular) como fallback
        System.out.println("WeatherCode não mapeado: " + weatherCode + ", usando nível 3");
        return 3;
    }

    /**
     * Retorna descrição textual do nível de clima.
     */
    public String getLevelDescription(int level) {
        return switch (level) {
            case 1 -> "Ótimo - Céu limpo";
            case 2 -> "Bom - Nublado";
            case 3 -> "Regular - Chuva leve/Nevoeiro";
            case 4 -> "Ruim - Chuva forte/Neve";
            case 5 -> "Crítico - Tempestades";
            default -> "Desconhecido";
        };
    }
}

