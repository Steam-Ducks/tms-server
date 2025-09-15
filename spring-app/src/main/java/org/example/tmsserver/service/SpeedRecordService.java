package org.example.tmsserver.service;

import org.example.tmsserver.dto.RadarApiResponse;
import org.example.tmsserver.entity.SpeedRecord;
import org.example.tmsserver.repository.SpeedRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class SpeedRecordService {

    private final SpeedRecordRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    public SpeedRecordService(SpeedRecordRepository repository) {
        this.repository = repository;
    }

    public void clearSpeedRecords() {
        repository.deleteAllRecords();
    }

    public void fetchAndSaveSpeedRecords() {
        try {
            OffsetDateTime now = OffsetDateTime.now();
            OffsetDateTime initial = now.minusMinutes(10);

            // Para teste
            String initialStr = "2025-08-16 15:05:00";

            // Para produção
            //String initialStr = initial.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String url = "https://mock-api-75a7.onrender.com/radares?initial_date=" + initialStr;

            RadarApiResponse response = restTemplate.getForObject(url, RadarApiResponse.class);

            if (response != null && response.getData() != null) {
                List<SpeedRecord> records = response.getData().stream().map(d -> {
                    SpeedRecord r = new SpeedRecord();
                    r.setCameraId(Long.parseLong(d.getCameraNumero().replaceAll("\\D", "")));
                    r.setSpeed(BigDecimal.valueOf(d.getVelocidade()));
                    r.setVehicleType(d.getTipoVeiculo());

                    LocalDateTime ldt = LocalDateTime.parse(d.getDataHoraTz());
                    r.setTime(ldt.atOffset(ZoneOffset.ofHours(-3))); // ajusta para seu fuso, ex: -3h

                    return r;
                }).toList();

                repository.saveAll(records);
            }

        } catch (HttpServerErrorException e) {
            System.err.println("Erro HTTP da API: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Erro ao processar registros: " + e.getMessage());
            e.printStackTrace();
        }
    }

}