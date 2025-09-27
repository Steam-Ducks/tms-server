package org.example.tmsserver.service;
import org.example.tmsserver.dto.ZoneLevelDTO;
import org.example.tmsserver.entity.AppLevel;
import org.example.tmsserver.entity.Indicator;
import org.example.tmsserver.entity.Region;
import org.example.tmsserver.entity.RegionIndicator;
import org.example.tmsserver.repository.IndicatorRepository;
import org.example.tmsserver.repository.RegionIndicatorRepository;
import org.example.tmsserver.repository.RegionRepository;
import org.example.tmsserver.repository.SpeedRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.tmsserver.repository.AppLevelRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegionIndicatorService {

    private final SpeedRecordRepository speedRecordRepository;
    private final RegionIndicatorRepository regionIndicatorRepository;
    private final RegionRepository regionRepository;
    private final IndicatorRepository indicatorRepository;
    private final AppLevelRepository appLevelRepository;

    public RegionIndicatorService(SpeedRecordRepository speedRecordRepository,
                                  RegionIndicatorRepository regionIndicatorRepository,
                                  RegionRepository regionRepository,
                                  AppLevelRepository appLevelRepository,
                                  IndicatorRepository indicatorRepository) {
        this.speedRecordRepository = speedRecordRepository;
        this.regionIndicatorRepository = regionIndicatorRepository;
        this.regionRepository = regionRepository;
        this.indicatorRepository = indicatorRepository;
        this.appLevelRepository = appLevelRepository;
    }

    @Transactional
    public void calculateAndSaveRegionIndicators() {
        System.out.println("Iniciando cálculo de indicadores...");

        calculateAverageSpeedIndicator();

        //future indicators here

        System.out.println("Processamento de todos os indicadores finalizado!");
    }

    private void calculateAverageSpeedIndicator() {
        System.out.println("Calculando indicador: Average Speed");

        Optional<Indicator> indicatorOpt = indicatorRepository.findByName("Average Speed");
        if (indicatorOpt.isEmpty()) {
            System.err.println("Indicator 'Average Speed' not found.");
            return;
        }
        Indicator indicatorEntity = indicatorOpt.get();

        List<Object[]> data = speedRecordRepository.findRegionCameraAggregates();
        System.out.println("Dados retornados: " + data.size() + " registros");

        Map<Integer, BigDecimal[]> regionMap = calculateAverageSpeedByRegion(data);

        saveRegionIndicators(regionMap, indicatorEntity);

        System.out.println("Indicador Average Speed processado!");
    }

    private Map<Integer, BigDecimal[]> calculateAverageSpeedByRegion(List<Object[]> data) {
        Map<Integer, BigDecimal[]> regionMap = new HashMap<>();

        for (Object[] row : data) {
            try {
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

                System.out.println("Processando regionId=" + regionId + ", cameraId=" + cameraId
                        + ", speedLimit=" + speedLimit + ", count=" + count + ", sumSpeed=" + sumSpeed);

                BigDecimal cameraAvg = sumSpeed.divide(BigDecimal.valueOf(count), 6, RoundingMode.HALF_UP);
                BigDecimal normalized = cameraAvg.divide(speedLimit, 6, RoundingMode.HALF_UP);

                regionMap.putIfAbsent(regionId, new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
                BigDecimal[] vals = regionMap.get(regionId);

                vals[0] = vals[0].add(normalized.multiply(BigDecimal.valueOf(count)));
                vals[1] = vals[1].add(BigDecimal.valueOf(count));
            } catch (Exception e) {
                System.err.println("Erro processando linha: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return regionMap;
    }

    private void saveRegionIndicators(Map<Integer, BigDecimal[]> regionMap, Indicator indicator) {
        OffsetDateTime now = OffsetDateTime.now();
        System.out.println("Cálculo por região finalizado. Criando RegionIndicators...");

        for (Map.Entry<Integer, BigDecimal[]> entry : regionMap.entrySet()) {
            Integer regionId = entry.getKey();
            BigDecimal[] vals = entry.getValue();

            try {
                Optional<Region> regionOpt = regionRepository.findById(regionId);
                if (regionOpt.isEmpty()) {
                    System.err.println("Region with ID " + regionId + " not found. Skipping.");
                    continue;
                }
                Region regionEntity = regionOpt.get();

                BigDecimal regionalAvg = BigDecimal.ZERO;
                if (vals[1].compareTo(BigDecimal.ZERO) > 0) {
                    regionalAvg = vals[0].divide(vals[1], 6, RoundingMode.HALF_UP);
                }

                RegionIndicator regionIndicator = new RegionIndicator();
                regionIndicator.setRegion(regionEntity);
                regionIndicator.setIndicator(indicator);
                regionIndicator.setValue(regionalAvg.multiply(BigDecimal.valueOf(100)).intValue());
                regionIndicator.setTime(now);
                regionIndicator.setChange("CALC");

                System.out.println("Preparando salvar: regionId=" + regionId + ", value=" + regionIndicator.getValue());

                try {
                    regionIndicatorRepository.save(regionIndicator);
                    regionIndicatorRepository.flush();
                    System.out.println("Salvo com sucesso: regionId=" + regionId);
                } catch (Exception e) {
                    System.err.println("Falha ao salvar indicator para regionId=" + regionId);
                    e.printStackTrace();
                }

            } catch (Exception e) {
                System.err.println("Erro criando RegionIndicator para regionId=" + regionId + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
     //s
   public List<ZoneLevelDTO> getLatestRegionLevels() {
    System.out.println("1. SERVICE: Recebi o pedido para buscar os últimos 6 níveis.");

    // 2. SERVICE -> REPOSITORY: Pedindo os 6 últimos registros da tabela APP_LEVEL.
    List<AppLevel> latestLevels = appLevelRepository.findTop6ByOrderByTimeDesc();
    System.out.println("4. SERVICE: Recebi " + latestLevels.size() + " registros do repositório.");

    // 5. SERVICE: Agora, vou transformar cada registro 'AppLevel' em um 'ZoneLevelDTO'.
    return latestLevels.stream().map(appLevel -> {
        
        // Para cada 'appLevel', preciso buscar o nome da região.
        // SERVICE -> REPOSITORY: Pedindo o nome da região com ID = appLevel.getIdRegion().
        String regionName = regionRepository.findById(appLevel.getIdRegion().intValue())
                                            .map(Region::getName) // Se encontrar, pega o nome.
                                            .orElse("Região Desconhecida"); // Se não, usa um nome padrão.

        System.out.println(" > Processando ID_REGION: " + appLevel.getIdRegion() + ", NOME: " + regionName + ", LEVEL: " + appLevel.getValue());

        // Criando o objeto final com todos os dados formatados.
        return new ZoneLevelDTO(
            String.valueOf(appLevel.getIdRegion()),
            regionName,
            appLevel.getValue() // O 'value' da APP_LEVEL já é o nível que queremos.
        );
    }).collect(Collectors.toList()); // Coleta tudo em uma lista final.
}
}
