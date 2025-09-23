package org.example.tmsserver.service;

import java.time.OffsetDateTime;

import org.example.tmsserver.repository.LevelRepository;
import org.example.tmsserver.repository.RegionIndicatorRepository;
import org.example.tmsserver.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.example.tmsserver.entity.Level;
import org.example.tmsserver.entity.Region;


import java.util.List;

@Service
public class LevelService {

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

    public Level calculateLevelForRegion(Long regionId) {
        List<Integer> values = regionIndicatorRepository.findValuesByRegion(regionId);

        if (values.isEmpty()) {
            throw new IllegalStateException("Nenhum indicador encontrado para a região " + regionId);
        }

        double avg = values.stream()
                           .mapToInt(Integer::intValue)
                           .average()
                           .orElse(0);

        int levelValue = mapAverageToLevel(avg);

        Region region = regionRepository.findById(regionId)
            .orElseThrow(() -> new IllegalArgumentException("Região não encontrada"));

        Level level = new Level();
        level.setValue(levelValue);
        level.setTime(OffsetDateTime.now());
        level.setRegion(region);

        return levelRepository.save(level);
    }

    private int mapAverageToLevel(double avg) {
        if (avg <= 20) return 1;
        if (avg <= 40) return 2;
        if (avg <= 60) return 3;
        if (avg <= 80) return 4;
        return 5;
    }
}
