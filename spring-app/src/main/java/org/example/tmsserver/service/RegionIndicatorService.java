package org.example.tmsserver.service;
import org.example.tmsserver.dto.RegionIndicatorDTO;
import org.example.tmsserver.dto.WeatherResponse;
import org.example.tmsserver.entity.Indicator;
import org.example.tmsserver.entity.Region;
import org.example.tmsserver.entity.RegionIndicator;
import org.example.tmsserver.repository.IndicatorRepository;
import org.example.tmsserver.repository.RegionIndicatorRepository;
import org.example.tmsserver.repository.RegionRepository;
import org.example.tmsserver.repository.SpeedRecordRepository;
import org.example.tmsserver.config.RegionWeatherConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@Service
public class RegionIndicatorService {

    private final SpeedRecordRepository speedRecordRepository;
    private final RegionIndicatorRepository regionIndicatorRepository;
    private final RegionRepository regionRepository;
    private final IndicatorRepository indicatorRepository;
    private final WeatherApiClient weatherApiClient;
    private final WeatherCodeMapper weatherCodeMapper;

    public RegionIndicatorService(SpeedRecordRepository speedRecordRepository,
                                  RegionIndicatorRepository regionIndicatorRepository,
                                  RegionRepository regionRepository,
                                  IndicatorRepository indicatorRepository,
                                  WeatherApiClient weatherApiClient,
                                  WeatherCodeMapper weatherCodeMapper) {
        this.speedRecordRepository = speedRecordRepository;
        this.regionIndicatorRepository = regionIndicatorRepository;
        this.regionRepository = regionRepository;
        this.indicatorRepository = indicatorRepository;
        this.weatherApiClient = weatherApiClient;
        this.weatherCodeMapper = weatherCodeMapper;
    }

    @Transactional
    public void calculateAndSaveRegionIndicators() {
        System.out.println("Iniciando cálculo de indicadores...");

        calculateAverageSpeedIndicator();

        calculateComplianceRateIndicator();

        calculateTrafficDensityIndicator();

        calculateWeatherIndicator();

        System.out.println("Processamento de todos os indicadores finalizado!");
    }

    // AVERAGE SPEED METHOD

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

    // COMPLIANCE RATE METHOD

    private void calculateComplianceRateIndicator() {
        System.out.println("Calculando indicador: Compliance Rate");

        Optional<Indicator> indicatorOpt = indicatorRepository.findByName("Compliance Rate");

        if (indicatorOpt.isEmpty()) {
            System.err.println("Indicator 'Compliance Rate' not found.");
            return;
        }

        Indicator indicatorEntity = indicatorOpt.get();

        List<Object[]> data = speedRecordRepository.findRegionCameraAggregates();
        System.out.println("Dados retornados: " + data.size() + " registros");

        Map<Integer, BigDecimal[]> regionMap = calculateComplianceRateByRegion(data);

        saveRegionIndicators(regionMap, indicatorEntity);

        System.out.println("Indicador Compliance Rate processado!");
    }

    private Map<Integer, BigDecimal[]> calculateComplianceRateByRegion(List<Object[]> data) {

        Map<Integer, BigDecimal[]> tempMap = new HashMap<>();

        for (Object[] row : data) {
            try {
                Integer regionId = ((Number) row[0]).intValue();
                BigDecimal countAbove = new BigDecimal(((Number) row[5]).longValue());
                BigDecimal totalCount = new BigDecimal(((Number) row[3]).longValue());

                tempMap.putIfAbsent(regionId, new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
                BigDecimal[] vals = tempMap.get(regionId);

                vals[0] = vals[0].add(countAbove);
                vals[1] = vals[1].add(totalCount);

            } catch (Exception e) {
                System.err.println("Erro processando linha: " + e.getMessage());
            }
        }

        Map<Integer, BigDecimal[]> complianceMap = new HashMap<>();
        for (Map.Entry<Integer, BigDecimal[]> entry : tempMap.entrySet()) {
            BigDecimal countAboveSum = entry.getValue()[0];
            BigDecimal totalCountSum = entry.getValue()[1];

            BigDecimal rate = countAboveSum.divide(totalCountSum, 6, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);

            complianceMap.put(entry.getKey(), new BigDecimal[]{rate, BigDecimal.valueOf(1)});
        }

        return complianceMap;
    }

    // TRAFFIC DENSITY METHOD

    private void calculateTrafficDensityIndicator() {
        System.out.println("Calculando indicador: Traffic Density");

        Optional<Indicator> indicatorOpt = indicatorRepository.findByName("Traffic Density");

        if (indicatorOpt.isEmpty()) {
            System.err.println("Indicator 'Traffic Density' not found.");
            return;
        }

        Indicator indicatorEntity = indicatorOpt.get();

        List<Object[]> data = speedRecordRepository.findRegionCameraAggregates();
        System.out.println("Dados retornados: " + data.size() + " registros");

        Map<Integer, BigDecimal[]> regionMap = calculateTrafficDensityByRegion(data);

        saveRegionIndicators(regionMap, indicatorEntity);

        System.out.println("Indicador Traffic Density processado!");
    }

    private Map<Integer, BigDecimal[]> calculateTrafficDensityByRegion(List<Object[]> data) {

        Map<Integer, BigDecimal> totalRecordsMap = new HashMap<>();
        Map<Integer, Set<String>> camerasMap = new HashMap<>();

        for (Object[] row : data) {
            Integer regionId = ((Number) row[0]).intValue();
            String cameraId = row[1].toString();
            BigDecimal count = new BigDecimal(((Number) row[3]).longValue());

            totalRecordsMap.put(regionId, totalRecordsMap.getOrDefault(regionId, BigDecimal.ZERO).add(count));

            camerasMap.putIfAbsent(regionId, new HashSet<>());
            camerasMap.get(regionId).add(cameraId);
        }

        Map<Integer, BigDecimal[]> trafficDensityMap = new HashMap<>();
        for (Integer regionId : totalRecordsMap.keySet()) {
            BigDecimal totalRecords = totalRecordsMap.get(regionId);
            BigDecimal numCameras = BigDecimal.valueOf(camerasMap.get(regionId).size());
            BigDecimal density = totalRecords
                    .divide(numCameras, 6, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

            trafficDensityMap.put(regionId, new BigDecimal[]{density, BigDecimal.valueOf(1)});
        }

        return trafficDensityMap;
    }

    // WEATHER INDICATOR METHOD

    private void calculateWeatherIndicator() {
        System.out.println("Calculando indicador: Weather");

        Optional<Indicator> indicatorOpt = indicatorRepository.findByName("Weather");
        if (indicatorOpt.isEmpty()) {
            System.err.println("Indicator 'Weather' not found.");
            return;
        }
        Indicator indicatorEntity = indicatorOpt.get();

        Map<Integer, BigDecimal[]> regionMap = calculateWeatherByRegion();

        saveRegionIndicators(regionMap, indicatorEntity);

        System.out.println("Indicador Weather processado!");
    }

    private Map<Integer, BigDecimal[]> calculateWeatherByRegion() {
        Map<Integer, BigDecimal[]> weatherMap = new HashMap<>();

        List<Region> regions = regionRepository.findAll();

        for (Region region : regions) {
            try {
                Integer regionId = region.getIdRegion();

                if (!RegionWeatherConfig.hasCoordinates(regionId)) {
                    System.err.println("No coordinates configured for regionId=" + regionId + ". Skipping.");
                    continue;
                }

                double[] coordinates = RegionWeatherConfig.getCoordinates(regionId);
                double latitude = coordinates[0];
                double longitude = coordinates[1];

                System.out.println("Buscando dados de clima para regionId=" + regionId +
                                 " (lat=" + latitude + ", lon=" + longitude + ")");

                WeatherResponse weatherResponse = weatherApiClient.getWeather(latitude, longitude);

                if (weatherResponse == null || weatherResponse.getCurrent() == null) {
                    System.err.println("Failed to get weather data for regionId=" + regionId);
                    continue;
                }

                int weatherCode = weatherResponse.getCurrent().getWeathercode();

                System.out.println("RegionId=" + regionId + " - WeatherCode=" + weatherCode);

                BigDecimal weatherCodeValue = BigDecimal.valueOf(weatherCode);

                weatherMap.put(regionId, new BigDecimal[]{weatherCodeValue, BigDecimal.valueOf(1)});

            } catch (Exception e) {
                System.err.println("Erro processando clima para regionId=" + region.getIdRegion() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        return weatherMap;
    }

    // SAVING INDICATORS IN DATABASE METHOD

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

                if ("Weather".equals(indicator.getName())) {
                    regionIndicator.setValue(regionalAvg.intValue());
                } else {
                    regionIndicator.setValue(regionalAvg.multiply(BigDecimal.valueOf(100)).intValue());
                }

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

    // GETTERS

    public List<RegionIndicatorDTO> getIndicatorPerDay() {
        return regionIndicatorRepository.mapToDTOPerDay();
    }

    public List<RegionIndicatorDTO> getIndicatorPerHour() {
        return regionIndicatorRepository.mapToDTOPerHour();
    }
}
