package org.example.tmsserver.job;

import org.example.tmsserver.service.SpeedRecordService;
import org.example.tmsserver.service.RegionIndicatorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SpeedDataImportJob {

    private final SpeedRecordService speedRecordService;
    private final RegionIndicatorService regionIndicatorService;

    public SpeedDataImportJob(SpeedRecordService speedRecordService,
                              RegionIndicatorService regionIndicatorService) {
        this.speedRecordService = speedRecordService;
        this.regionIndicatorService = regionIndicatorService;
    }

    // Para teste, cron = "0 * * * * *" repete a cada minuto
    @Scheduled(cron = "0 * * * * *")
    public void execute() {
        speedRecordService.clearSpeedRecords();
        speedRecordService.fetchAndSaveSpeedRecords();
        System.out.println("Speed records atualizados!");

        // Chama cálculo de indicadores
        regionIndicatorService.calculateAndSaveRegionIndicators();
        System.out.println("Region indicators calculados e salvos!");
    }
}