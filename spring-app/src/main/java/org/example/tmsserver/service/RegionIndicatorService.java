package org.example.tmsserver.service;

import org.example.tmsserver.entity.RegionIndicator;
import org.example.tmsserver.repository.RegionIndicatorRepository;
import org.example.tmsserver.repository.SpeedRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
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

        Map<Integer, BigDecimal[]> regionMap = new HashMap<>();

        for (Object[] row : data) {

            Integer regionId = row[0] != null ? ((Number) row[0]).intValue() : null;
            String cameraId = row[1] != null ? row[1].toString() : null;
            BigDecimal speedLimit = null;
            if (row[2] != null) {
                if (row[2] instanceof BigDecimal) {
                    speedLimit = (BigDecimal) row[2];
                } else if (row[2] instanceof Number) {
                    speedLimit = BigDecimal.valueOf(((Number) row[2]).doubleValue());
                }
            }
            Long count = row[3] != null ? ((Number) row[3]).longValue() : null;
            BigDecimal sumSpeed = row[4] instanceof BigDecimal ? (BigDecimal) row[4] : null;

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
                    Integer regionId = entry.getKey();
                    BigDecimal[] vals = entry.getValue();

                    BigDecimal regionalAvg = BigDecimal.ZERO;
                    if (vals[1].compareTo(BigDecimal.ZERO) > 0) {
                        regionalAvg = vals[0].divide(vals[1], 6, RoundingMode.HALF_UP);
                    }

                    RegionIndicator indicator = new RegionIndicator();
                    indicator.setIdRegion(regionId);
                    indicator.setIdIndicator(1);
                    indicator.setValue(regionalAvg.multiply(BigDecimal.valueOf(100)).intValue());
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