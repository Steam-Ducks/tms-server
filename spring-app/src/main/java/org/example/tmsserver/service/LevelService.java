package org.example.tmsserver.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.example.tmsserver.dto.ZoneLevelDTO;
import org.example.tmsserver.entity.Level;
import org.example.tmsserver.entity.Region;
import org.example.tmsserver.entity.RegionIndicator;
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

    public LevelService(RegionIndicatorRepository regionIndicatorRepository,
                        LevelRepository levelRepository,
                        RegionRepository regionRepository, IndicatorRepository indicatorRepository) {
        this.regionIndicatorRepository = regionIndicatorRepository;
        this.levelRepository = levelRepository;
        this.regionRepository = regionRepository;
        this.indicatorRepository = indicatorRepository;
    }

    public List<ZoneLevelDTO> getLatestRegionLevels() {

        List<Level> latestLevels = levelRepository.findTop6ByOrderByTimeDesc();

        return latestLevels.stream().map(level -> {

            String regionName = regionRepository.findById(level.getRegion().getIdRegion())
                    .map(Region::getName)
                    .orElse("Região Desconhecida");


            return new ZoneLevelDTO(
                    String.valueOf(level.getRegion().getIdRegion()),
                    regionName,
                    level.getValue()
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
        List<Integer> indicators = new ArrayList<>();
        // Buscar valores do indicador
        List<RegionIndicator> regionIndicators = regionIndicatorRepository.findValuesByRegion(regionId, countIndicators());

        RegionIndicator averageSpeedIndicator = regionIndicators.stream()
            .filter(ri -> "Average Speed".equals(ri.getIndicator().getName()))
            .findFirst()
            .orElse(null);

        if (averageSpeedIndicator == null) {
            System.out.println("DEBUG: Indicador AverageSpeed não encontrado para a região -> " + regionId);
            logger.warn("Indicador AverageSpeed não encontrado para a região {}", regionId);
        }

        int averageSpeedLevel = mapAverageSpeedToLevel(averageSpeedIndicator.getValue());
        indicators.add(averageSpeedLevel);

        int levelValue = accumulateLevel(indicators);

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

        // Criar e salvar Level
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

    private int accumulateLevel(List<Integer> indicators) {
        double average = indicators.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        int floor = (int) Math.floor(average);
        double decimal = average - floor;

        if (decimal < 0.4) {
            return floor;
        } else {
            return floor + 1;
        }

    }


    private int mapAverageSpeedToLevel(int avg) {
        if (avg >= 77) return 1;
        if (avg >= 70) return 2;
        if (avg >= 60) return 3;
        if (avg >= 55) return 4;
        return 5;
    }

    private int countIndicators() {
        return (int) indicatorRepository.count();
    }
}
