package org.example.tmsserver.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.example.tmsserver.entity.Level;
import org.example.tmsserver.entity.Region;
import org.example.tmsserver.repository.LevelRepository;
import org.example.tmsserver.repository.RegionIndicatorRepository;
import org.example.tmsserver.repository.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class LevelService {

    private static final Logger logger = LoggerFactory.getLogger(LevelService.class);

    private final RegionIndicatorRepository regionIndicatorRepository;
    private final LevelRepository levelRepository;
    private final RegionRepository regionRepository;

    public LevelService(RegionIndicatorRepository regionIndicatorRepository,
                        LevelRepository levelRepository,
                        RegionRepository regionRepository) {
        this.regionIndicatorRepository = regionIndicatorRepository;
        this.levelRepository = levelRepository;
        this.regionRepository = regionRepository;
    }
    @Transactional
    public Level calculateLevelForRegion(Integer regionId) {
        logger.info("=== Início do cálculo de nível para região ID: {} ===", regionId);
        System.out.println("DEBUG: Recebido regionId -> " + regionId);

        // Buscar valores do indicador
        List<Integer> values = regionIndicatorRepository.findValuesByRegion(regionId);
        System.out.println("DEBUG: Valores obtidos: " + values);
        logger.info("Valores do indicador: {}", values);

        if (values.isEmpty()) {
            logger.warn("Nenhum indicador encontrado para a região {}", regionId);
            throw new IllegalStateException("Nenhum indicador encontrado para a região " + regionId);
        }

        // Calcular média
        double avg = values.stream()
                           .mapToInt(Integer::intValue)
                           .average()
                           .orElse(0);
        System.out.println("DEBUG: Média calculada -> " + avg);
        logger.info("Média dos indicadores: {}", avg);

        // Mapear para nível
        int levelValue = mapAverageToLevel(avg);
        System.out.println("DEBUG: Nível mapeado -> " + levelValue);
        logger.info("Nível determinado: {}", levelValue);

        // Buscar região pelo ID
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

    private int mapAverageToLevel(double avg) {
        if (avg <= 20) return 1;
        if (avg <= 40) return 2;
        if (avg <= 60) return 3;
        if (avg <= 80) return 4;
        return 5;
    }
}
