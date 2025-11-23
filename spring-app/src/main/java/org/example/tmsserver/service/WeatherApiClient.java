package org.example.tmsserver.service;

import org.example.tmsserver.dto.WeatherResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherApiClient {

    private final RestTemplate restTemplate;

    public WeatherApiClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public WeatherResponse getWeather(double latitude, double longitude) {
        String url = String.format(
            "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current=weathercode",
            latitude, longitude
        );

        try {
            System.out.println("Chamando Open Meteo API: " + url);
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            System.out.println("Resposta recebida: " + (response != null ? "OK" : "NULL"));
            return response;
        } catch (Exception e) {
            System.err.println("Erro ao buscar dados de clima: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

