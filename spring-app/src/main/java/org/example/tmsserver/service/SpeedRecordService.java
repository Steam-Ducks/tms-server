package org.example.tmsserver.service;

import org.example.tmsserver.dto.RadarApiResponse;
import org.example.tmsserver.entity.Camera;
import org.example.tmsserver.entity.Region;
import org.example.tmsserver.entity.SpeedRecord;
import org.example.tmsserver.repository.CameraRepository;
import org.example.tmsserver.repository.SpeedRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.example.tmsserver.repository.RegionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

@Service
public class SpeedRecordService {

    private final SpeedRecordRepository speedRecordRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final CameraRepository cameraRepository;
    private final RegionRepository regionRepository;

    public SpeedRecordService(SpeedRecordRepository speedRecordRepository, CameraRepository cameraRepository, RegionRepository regionRepository) {
        this.speedRecordRepository = speedRecordRepository;
        this.cameraRepository = cameraRepository;
        this.regionRepository = regionRepository;
    }

    public void clearSpeedRecords() {
        speedRecordRepository.deleteAllRecords();
    }

    @Transactional
    public void fetchAndSaveSpeedRecords() {
        try {
            OffsetDateTime now = OffsetDateTime.now();
            OffsetDateTime initial = now.minusMinutes(10);

            // Para teste
            // Para teste - gera horário aleatório no mesmo dia
            Random random = new Random();
            int randomHour = random.nextInt(24);   // 0-23
            int randomMinute = random.nextInt(60); // 0-59
            int randomSecond = random.nextInt(60); // 0-59

            String initialStr = String.format("2025-08-16 %02d:%02d:%02d", randomHour, randomMinute, randomSecond);
            System.out.println("Usando horário aleatório para teste: " + initialStr);

            // String initialStr = initial.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String url = "https://mock-api-75a7.onrender.com/radares?initial_date=" + initialStr + "&last_minutes=1";
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

                            BigDecimal lat = d.getLatitude();
                            BigDecimal lon = d.getLongitude();
                            Integer regionId = regionRepository.findRegionByPoint(lat, lon);

                            if (regionId == null) {
                                System.err.println("⚠️ Nenhuma região encontrada para coordenadas: lat=" + lat + ", lon=" + lon + ". Pulando registro.");
                                return null;
                            }


                            Region region = null;
                            if (regionId != null) {
                                region = regionRepository.findById(regionId)
                                        .orElse(null);
                            }

                            camera = new Camera();
                            camera.setIdCamera(cameraId);
                            camera.setLatitude(lat);
                            camera.setLongitude(lon);
                            camera.setBairro(d.getEndereco());
                            camera.setSpeedLimit(d.getLimite());
                            camera.setRegion(region);

                            cameraRepository.save(camera);
                            cameras.add(camera);

                            System.out.println("Nova câmera cadastrada: " + camera.getIdCamera() +
                                    ", Região: " + (region != null ? region.getIdRegion() : "null") +
                                    ", Latitude: " + camera.getLatitude() +
                                    ", Longitude: " + camera.getLongitude());
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
                speedRecordRepository.saveAllInBatches(records);
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