package org.example.tmsserver.service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.example.tmsserver.dto.ZoneLevelDTO;
import org.example.tmsserver.dto.WorstStreetByRegionDTO;
import org.example.tmsserver.entity.Level;
import org.example.tmsserver.entity.Region;
import org.example.tmsserver.entity.RegionIndicator;
import org.example.tmsserver.indicators.*;
import org.example.tmsserver.repository.CameraRepository;
import org.example.tmsserver.repository.IndicatorRepository;
import org.example.tmsserver.repository.LevelRepository;
import org.example.tmsserver.repository.RegionIndicatorRepository;
import org.example.tmsserver.repository.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LevelService {

    private static final Logger logger = LoggerFactory.getLogger(LevelService.class);

    private final RegionIndicatorRepository regionIndicatorRepository;
    private final LevelRepository levelRepository;
    private final RegionRepository regionRepository;
    private final IndicatorRepository indicatorRepository;
    private final CameraRepository cameraRepository;
    private final AlertMonitorService alertMonitorService;

    private static final Map<String, Double> INDICATOR_WEIGHTS = Map.of(
            "Average Speed", 0.4,
            "Compliance Rate", 0.25,
            "Traffic Density", 0.2,
            "Weather", 0.15
    );

    public LevelService(RegionIndicatorRepository regionIndicatorRepository,
                        LevelRepository levelRepository,
                        RegionRepository regionRepository, IndicatorRepository indicatorRepository,
                        CameraRepository cameraRepository, AlertMonitorService alertMonitorService) {
        this.regionIndicatorRepository = regionIndicatorRepository;
        this.levelRepository = levelRepository;
        this.regionRepository = regionRepository;
        this.indicatorRepository = indicatorRepository;
        this.cameraRepository = cameraRepository;
        this.alertMonitorService = alertMonitorService;
    }

    public List<ZoneLevelDTO> getLatestRegionLevels() {
        List<Level> latestLevels = levelRepository.findTop6ByOrderByTimeDesc();

        return latestLevels.stream().map(level -> {
            Integer regionId = level.getRegion().getIdRegion();

            String regionName = regionRepository.findById(regionId)
                    .map(Region::getName)
                    .orElse("Região Desconhecida");

            Integer latestWeatherCode = getLatestWeatherCodeForRegion(regionId);

            List<Object[]> cameraData = cameraRepository.findCamerasWithStatsForRegion(regionId);

            Map<String, List<Object[]>> groupedCameras = cameraData.stream()
                    .collect(Collectors.groupingBy(row -> {
                        String cameraId = row[0] != null ? row[0].toString() : "";
                        return cameraId.replaceAll("_\\d+$", "");
                    }));

            List<Map<String, Object>> cameras = groupedCameras.entrySet().stream().map(entry -> {
                String baseCameraId = entry.getKey();
                List<Object[]> cameraGroup = entry.getValue();

                Map<String, Object> cameraMap = new HashMap<>();

                Object[] firstCamera = cameraGroup.get(0);
                java.math.BigDecimal latitude = firstCamera[1] != null ? (java.math.BigDecimal) firstCamera[1] : null;
                java.math.BigDecimal longitude = firstCamera[2] != null ? (java.math.BigDecimal) firstCamera[2] : null;
                String address = firstCamera[3] != null ? firstCamera[3].toString() : null;
                Integer speedLimit = firstCamera[4] != null ? ((Number) firstCamera[4]).intValue() : null;

                Double avgSpeed = cameraGroup.stream()
                        .filter(row -> row[5] != null)
                        .mapToDouble(row -> ((Number) row[5]).doubleValue())
                        .average()
                        .orElse(0.0);

                cameraMap.put("id", baseCameraId);
                cameraMap.put("latitude", latitude);
                cameraMap.put("longitude", longitude);
                cameraMap.put("address", address);
                cameraMap.put("averageSpeed", Math.round(avgSpeed * 100.0) / 100.0);
                cameraMap.put("maxSpeed", speedLimit);

                return cameraMap;
            }).collect(Collectors.toList());

            return new ZoneLevelDTO(
                    String.valueOf(regionId),
                    regionName,
                    level.getValue(),
                    cameras,
                    latestWeatherCode
            );
        }).collect(Collectors.toList());
    }

    // NOVO MÉTODO: PIOR RUA
    // ----------------------------------------------------------------------------------

    public List<WorstStreetByRegionDTO> getWorstStreetsByRegion() {

        List<Level> latestLevels = levelRepository.findTop6ByOrderByTimeDesc();

        return latestLevels.stream().map(level -> {

            Integer regionId = level.getRegion().getIdRegion();
            String regionName = level.getRegion().getName();
            Integer regionLevel = level.getValue();

            List<Object[]> cameraData = cameraRepository.findCamerasWithStatsForRegion(regionId);

            // se não houver câmeras para a região → ainda retorna a região
            if (cameraData == null || cameraData.isEmpty()) {
                return new WorstStreetByRegionDTO(
                        String.valueOf(regionId),
                        regionName,
                        regionLevel,
                        null,
                        null,
                        null,
                        null,
                        null
                );
            }

            // agrupa por id base (rua)
            Map<String, List<Object[]>> grouped = cameraData.stream()
                    .collect(Collectors.groupingBy(row ->
                            row[0].toString().replaceAll("_\\d+$", "")
                    ));

            // calcula severidade por rua
            List<WorstStreetByRegionDTO> streets = grouped.entrySet().stream().map(e -> {

                String streetId = e.getKey();
                List<Object[]> group = e.getValue();

                Object[] first = group.get(0);
                String address = first[3] != null ? first[3].toString() : null;
                Integer speedLimit = first[4] != null ? ((Number) first[4]).intValue() : null;

                double avgSpeed = group.stream()
                        .filter(c -> c[5] != null)
                        .mapToDouble(c -> ((Number) c[5]).doubleValue())
                        .average()
                        .orElse(0.0);

                double severity = speedLimit != null ? (speedLimit - avgSpeed) : 0.0;

                return new WorstStreetByRegionDTO(
                        String.valueOf(regionId),
                        regionName,
                        regionLevel,
                        streetId,
                        address,
                        Math.round(avgSpeed * 100.0) / 100.0,
                        speedLimit,
                        Math.round(severity * 100.0) / 100.0
                );

            }).collect(Collectors.toList());

            // retorna apenas a pior rua
            return streets.stream()
                    .max(Comparator.comparingDouble(WorstStreetByRegionDTO::getSeverity))
                    .orElse(null);

        }).collect(Collectors.toList());
    }

    private Integer getLatestWeatherCodeForRegion(Integer regionId) {
        try {
            Optional<org.example.tmsserver.entity.Indicator> weatherIndicatorOpt =
                    indicatorRepository.findByName("Weather");

            if (weatherIndicatorOpt.isEmpty()) {
                logger.warn("Weather indicator not found in database");
                return null;
            }

            List<RegionIndicator> weatherIndicators = regionIndicatorRepository
                    .findByRegionIdRegionAndIndicatorOrderByTimeDesc(regionId, weatherIndicatorOpt.get());

            if (!weatherIndicators.isEmpty()) {
                Integer weatherCode = weatherIndicators.get(0).getValue();
                logger.debug("Latest weather code for region {}: {}", regionId, weatherCode);
                return weatherCode;
            } else {
                logger.warn("No weather data found for region {}", regionId);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error getting weather code for region {}: {}", regionId, e.getMessage());
            return null;
        }
    }

    @Transactional
    public void calculateLevelsForAllRegions() {
        List<Region> allRegions = regionRepository.findAll();

        for (Region region : allRegions) {
            calculateLevelForRegion(region.getIdRegion());
        }
    }

    @Transactional
    public Level calculateLevelForRegion(Integer regionId) {

        List<IndicatorCalculator> calculators = List.of(
                new AverageSpeedCalculator(),
                new ComplianceRateCalculator(),
                new TrafficDensityCalculator(),
                new WeatherCalculator()
        );

        List<RegionIndicator> regionIndicators =
                regionIndicatorRepository.findValuesByRegion(regionId, countIndicators());

        Map<String, Integer> indicatorLevels = new HashMap<>();

        for (IndicatorCalculator calculator : calculators) {
            RegionIndicator ri = regionIndicators.stream()
                    .filter(r -> r.getIndicator().getName().equals(calculator.getIndicatorName()))
                    .findFirst()
                    .orElse(null);

            if (ri != null) {
                indicatorLevels.put(calculator.getIndicatorName(),
                        calculator.mapValueToLevel(ri.getValue()));
            }
        }

        int levelValue = accumulateLevel(indicatorLevels);

        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("Região não encontrada"));

        Level level = new Level();
        level.setValue(levelValue);
        level.setTime(OffsetDateTime.now());
        level.setRegion(region);

        Level savedLevel = levelRepository.save(level);

        checkAndTriggerAlerts(region, levelValue);

        return savedLevel;
    }

    private int accumulateLevel(Map<String, Integer> indicatorLevels) {
        double weightedSum = 0.0;
        double totalWeight = 0.0;

        for (Map.Entry<String, Integer> entry : indicatorLevels.entrySet()) {
            String indicatorName = entry.getKey();
            int level = entry.getValue();
            double weight = INDICATOR_WEIGHTS.getOrDefault(indicatorName, 1.0);

            weightedSum += level * weight;
            totalWeight += weight;
        }

        double result = totalWeight > 0 ? weightedSum / totalWeight : 0.0;

        int floor = (int) Math.floor(result);
        double decimal = result - floor;

        return (decimal < 0.4) ? floor : floor + 1;
    }

    public Integer getCityLevel() {
        List<Level> latestLevels = levelRepository.findTop6ByOrderByTimeDesc();

        double average = latestLevels.stream()
                .mapToInt(Level::getValue)
                .average()
                .orElse(5.0);

        return (int) Math.round(average);
    }

    private int countIndicators() {
        return (int) indicatorRepository.count();
    }

    private void checkAndTriggerAlerts(Region region, int levelValue) {
        if (levelValue >= 4) {
            alertMonitorService.sendCriticalAlert(
                    region,
                    levelValue,
                    getSeverityDescription(levelValue),
                    getLevelDescription(levelValue)
            );
        }
    }

    private String getSeverityDescription(int level) {
        return switch (level) {
            case 4 -> "ALTO";
            case 5 -> "MUITO ALTO";
            default -> "ALTO";
        };
    }

    private String getLevelDescription(int level) {
        return switch (level) {
            case 4 -> "Tráfego intenso";
            case 5 -> "Congestionamento crítico";
            default -> "Tráfego intenso";
        };
    }
}
