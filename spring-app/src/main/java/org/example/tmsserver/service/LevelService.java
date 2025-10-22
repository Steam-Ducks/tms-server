package org.example.tmsserver.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.tmsserver.dto.ZoneLevelDTO;
import org.example.tmsserver.entity.Level;
import org.example.tmsserver.entity.Region;
import org.example.tmsserver.entity.RegionIndicator;
import org.example.tmsserver.indicators.AverageSpeedCalculator;
import org.example.tmsserver.indicators.ComplianceRateCalculator;
import org.example.tmsserver.indicators.IndicatorCalculator;
import org.example.tmsserver.indicators.TrafficDensityCalculator;
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

    private static final Map<String, Double> INDICATOR_WEIGHTS = Map.of(
            "Average Speed", 0.5,
            "Compliance Rate", 0.3,
            "Traffic Density", 0.2
    );

    public LevelService(RegionIndicatorRepository regionIndicatorRepository,
                        LevelRepository levelRepository,
                        RegionRepository regionRepository, IndicatorRepository indicatorRepository,
                        CameraRepository cameraRepository) {
        this.regionIndicatorRepository = regionIndicatorRepository;
        this.levelRepository = levelRepository;
        this.regionRepository = regionRepository;
        this.indicatorRepository = indicatorRepository;
        this.cameraRepository = cameraRepository;
    }

    public List<ZoneLevelDTO> getLatestRegionLevels() {
        List<Level> latestLevels = levelRepository.findTop6ByOrderByTimeDesc();

        return latestLevels.stream().map(level -> {
            Integer regionId = level.getRegion().getIdRegion();

            String regionName = regionRepository.findById(regionId)
                    .map(Region::getName)
                    .orElse("Região Desconhecida");

            // Get camera data for this region
            List<Object[]> cameraData = cameraRepository.findCamerasWithStatsForRegion(regionId);

            // Group cameras by base ID (remove _1, _2, etc. suffixes) and aggregate their speeds
            Map<String, List<Object[]>> groupedCameras = cameraData.stream()
                .collect(Collectors.groupingBy(row -> {
                    String cameraId = row[0] != null ? row[0].toString() : "";
                    // Remove suffix like _1, _2, _3, etc.
                    return cameraId.replaceAll("_\\d+$", "");
                }));

            List<Map<String, Object>> cameras = groupedCameras.entrySet().stream().map(entry -> {
                String baseCameraId = entry.getKey();
                List<Object[]> cameraGroup = entry.getValue();

                Map<String, Object> cameraMap = new HashMap<>();

                // Use data from the first camera in the group for static fields
                Object[] firstCamera = cameraGroup.get(0);
                java.math.BigDecimal latitude = firstCamera[1] != null ? (java.math.BigDecimal) firstCamera[1] : null;
                java.math.BigDecimal longitude = firstCamera[2] != null ? (java.math.BigDecimal) firstCamera[2] : null;
                String address = firstCamera[3] != null ? firstCamera[3].toString() : null;
                Integer speedLimit = firstCamera[4] != null ? ((Number) firstCamera[4]).intValue() : null;

                // Calculate average speed across all cameras in the group
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
                    cameras
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public void calculateLevelsForAllRegions() {
        logger.info("=== Início do cálculo de níveis para todas as regiões ===");

        List<Region> allRegions = regionRepository.findAll();
        logger.info("Total de regiões encontradas: {}", allRegions.size());

        for (Region region : allRegions) {
            try {
                logger.info("Calculando nível para região: {} (ID: {})", region.getName(), region.getIdRegion());
                calculateLevelForRegion(region.getIdRegion());
            } catch (Exception e) {
                logger.error("Erro ao calcular nível para região ID {}: {}", region.getIdRegion(), e.getMessage());
            }
        }

        logger.info("=== Fim do cálculo de níveis para todas as regiões ===");
    }

    @Transactional
    public Level calculateLevelForRegion(Integer regionId) {

        logger.info("=== Início do cálculo de nível para região ID: {} ===", regionId);
        System.out.println("DEBUG: Recebido regionId -> " + regionId);

        List<IndicatorCalculator> calculators = List.of(
                new AverageSpeedCalculator(),
                new ComplianceRateCalculator(),
                new TrafficDensityCalculator()
        );

        List<RegionIndicator> regionIndicators = regionIndicatorRepository.findValuesByRegion(regionId, countIndicators());

        Map<String, Integer> indicatorLevels = new HashMap<>();

        for (IndicatorCalculator calculator : calculators) {
            RegionIndicator ri = regionIndicators.stream()
                    .filter(r -> r.getIndicator().getName().equals(calculator.getIndicatorName()))
                    .findFirst()
                    .orElse(null);

            if (ri != null) {
                indicatorLevels.put(calculator.getIndicatorName(), calculator.mapValueToLevel(ri.getValue()));
            } else {
                logger.warn("Indicador {} não encontrado para a região {}", calculator.getIndicatorName(), regionId);
            }
        }

        int levelValue = accumulateLevel(indicatorLevels);

        System.out.println("DEBUG: Nível mapeado -> " + levelValue);
        logger.info("Nível determinado: {}", levelValue);


        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> {
                    System.out.println("DEBUG: Região não encontrada para o ID -> " + regionId);
                    logger.error("Região não encontrada para ID {}", regionId);
                    return new IllegalArgumentException("Região não encontrada");
                });
        System.out.println("DEBUG: Região encontrada: " + region.getName());
        logger.info("Região encontrada: {}", region.getName());

        Level level = new Level();
        level.setValue(levelValue);
        level.setTime(OffsetDateTime.now());
        level.setRegion(region);

        Level savedLevel = levelRepository.save(level);
        System.out.println("DEBUG: Level salvo -> " + savedLevel);
        logger.info("Level salvo com sucesso: {}", savedLevel);

        logger.info("=== Fim do cálculo de nível para região ID: {} ===", regionId);
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

        double weightedAverage = totalWeight > 0 ? weightedSum / totalWeight : 0.0;

        int floor = (int) Math.floor(weightedAverage);
        double decimal = weightedAverage - floor;

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
}
