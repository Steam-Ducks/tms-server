package org.example.tmsserver.service;

import org.example.tmsserver.dto.RadarApiResponse;
import org.example.tmsserver.entity.Camera;
import org.example.tmsserver.entity.SpeedRecord;
import org.example.tmsserver.repository.CameraRepository;
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

    private final SpeedRecordRepository speedRecordRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final CameraRepository cameraRepository;

    public SpeedRecordService(SpeedRecordRepository speedRecordRepository, CameraRepository cameraRepository) {
        this.speedRecordRepository = speedRecordRepository;
        this.cameraRepository = cameraRepository;
    }

    public void clearSpeedRecords() {
        speedRecordRepository.deleteAllRecords();
    }

    public void fetchAndSaveSpeedRecords() {
        try {
            OffsetDateTime now = OffsetDateTime.now();
            OffsetDateTime initial = now.minusMinutes(10);

            // Para teste
            String initialStr = "2025-08-16 15:05:00";
            // Para produção
            // String initialStr = initial.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String url = "https://mock-api-75a7.onrender.com/radares?initial_date=" + initialStr;
            RadarApiResponse response = restTemplate.getForObject(url, RadarApiResponse.class);

            if (response == null || response.getData() == null) return;

            // Lista de IDs das câmeras
            List<String> cameraIds = response.getData().stream()
                    .map(d -> d.getCameraNumero().trim())
                    .distinct()
                    .toList();

            // Busca das câmeras no banco
            List<Camera> cameras = cameraRepository.findCamerasByIdList(cameraIds);
            System.out.println("Câmeras carregadas do banco: " + cameras.stream()
                    .map(Camera::getIdCamera)
                    .toList());

            // Mapeamento dos registros
            List<SpeedRecord> records = response.getData().stream()
                    .map(d -> {
                        String cameraId = d.getCameraNumero().trim();
                        Camera camera = cameras.stream()
                                .filter(c -> c.getIdCamera().equals(cameraId))
                                .findFirst()
                                .orElse(null);

                        if (camera == null) {
                            System.err.println("Câmera do radar não encontrada no banco: " + cameraId);
                            return null;
                        }

                        // Parse seguro da data
                        OffsetDateTime time;
                        try {
                            LocalDateTime ldt = LocalDateTime.parse(d.getDataHoraTz());
                            time = ldt.atOffset(ZoneOffset.ofHours(-3));
                        } catch (Exception ex) {
                            System.err.println("Falha ao parsear data: " + d.getDataHoraTz());
                            return null;
                        }

                        SpeedRecord r = new SpeedRecord();
                        r.setCamera(camera);
                        r.setSpeed(BigDecimal.valueOf(d.getVelocidade()));
                        r.setVehicleType(d.getTipoVeiculo());
                        r.setTime(time);
                        return r;
                    })
                    .filter(r -> r != null)
                    .toList();

            if (!records.isEmpty()) {
                speedRecordRepository.saveAll(records);
                System.out.println("Indicadores salvos com sucesso! Total: " + records.size());
            }

        } catch (HttpServerErrorException e) {
            System.err.println("Erro HTTP da API: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Erro ao processar registros: " + e.getMessage());
            e.printStackTrace();
        }
    }

}