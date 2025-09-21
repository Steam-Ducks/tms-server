package org.example.tmsserver.service;

import org.example.tmsserver.entity.RegionIndicator;
import org.example.tmsserver.repository.RegionIndicatorRepository;
import org.example.tmsserver.repository.SpeedRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegionIndicatorService {

    private final SpeedRecordRepository speedRecordRepository;
    private final RegionIndicatorRepository regionIndicatorRepository;

    public RegionIndicatorService(SpeedRecordRepository speedRecordRepository,
                                  RegionIndicatorRepository regionIndicatorRepository) {
        this.speedRecordRepository = speedRecordRepository;
        this.regionIndicatorRepository = regionIndicatorRepository;
    }

    @Transactional
    public void calculateAndSaveRegionIndicators() {
        List<Object[]> data = speedRecordRepository.findRegionCameraAggregates();

        Map<Long, BigDecimal[]> regionMap = new HashMap<>();

        for (Object[] row : data) {

            // Proteção contra null e tipos inesperados
            Long regionId = row[0] instanceof Long ? (Long) row[0] : null;
            String cameraId = row[1] != null ? row[1].toString() : null;
            BigDecimal speedLimit = row[2] instanceof BigDecimal ? (BigDecimal) row[2] : null;
            Long count = row[3] instanceof Long ? (Long) row[3] : null;
            BigDecimal sumSpeed = row[4] instanceof BigDecimal ? (BigDecimal) row[4] : null;

            if (regionId == null || speedLimit == null || speedLimit.compareTo(BigDecimal.ZERO) <= 0
                    || count == null || count <= 0 || sumSpeed == null) {
                System.out.println("Ignorando registro inválido: " +
                        "Region=" + regionId +
                        ", Camera=" + cameraId +
                        ", speedLimit=" + speedLimit +
                        ", count=" + count +
                        ", sumSpeed=" + sumSpeed);
                continue;
            }

            BigDecimal cameraAvg = sumSpeed.divide(BigDecimal.valueOf(count), 6, RoundingMode.HALF_UP);
            BigDecimal normalized = cameraAvg.divide(speedLimit, 6, RoundingMode.HALF_UP);

            regionMap.putIfAbsent(regionId, new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            BigDecimal[] vals = regionMap.get(regionId);

            vals[0] = vals[0].add(normalized.multiply(BigDecimal.valueOf(count)));
            vals[1] = vals[1].add(BigDecimal.valueOf(count));

        }

        OffsetDateTime now = OffsetDateTime.now();

        List<RegionIndicator> indicators = regionMap.entrySet().stream()
                .map(entry -> {
                    Long regionId = entry.getKey();
                    BigDecimal[] vals = entry.getValue();

                    BigDecimal regionalAvg = BigDecimal.ZERO;
                    if (vals[1].compareTo(BigDecimal.ZERO) > 0) {
                        regionalAvg = vals[0].divide(vals[1], 6, RoundingMode.HALF_UP);
                    }

                    RegionIndicator indicator = new RegionIndicator();
                    indicator.setIdRegion(regionId);
                    indicator.setIdIndicator(1L);
                    indicator.setValue(regionalAvg);
                    indicator.setTime(now);
                    indicator.setChange("CALC");

                    return indicator;
                })
                .toList();

        try {
            regionIndicatorRepository.saveAll(indicators);
            regionIndicatorRepository.flush();
            System.out.println("Indicadores salvos com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao salvar indicadores: " + e.getMessage());
            e.printStackTrace();
        }
    }
}