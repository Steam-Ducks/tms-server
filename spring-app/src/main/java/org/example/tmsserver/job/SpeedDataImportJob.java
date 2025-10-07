package org.example.tmsserver.job;

import org.example.tmsserver.service.SpeedRecordService;
import org.example.tmsserver.service.RegionIndicatorService;
import org.example.tmsserver.service.LevelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SpeedDataImportJob {

    private final SpeedRecordService speedRecordService;
    private final RegionIndicatorService regionIndicatorService;
    private final LevelService levelService;

    @Value("${server.role:MAIN}")
    private String serverRole;

    public SpeedDataImportJob(SpeedRecordService speedRecordService,
            RegionIndicatorService regionIndicatorService,
            LevelService levelService) {
        this.speedRecordService = speedRecordService;
        this.regionIndicatorService = regionIndicatorService;
        this.levelService = levelService;
    }

    // Para teste, cron = "0 * * * * *" repete a cada minuto
    @Scheduled(cron = "0 * * * * *")
    public void execute() {
        if (!"MAIN".equalsIgnoreCase(serverRole)) {
            return;
        }

        System.out.println("Inicializado importação de registros!");

        speedRecordService.clearSpeedRecords();
        speedRecordService.fetchAndSaveSpeedRecords();
        regionIndicatorService.calculateAndSaveRegionIndicators();
        levelService.calculateLevelsForAllRegions();

        System.out.println("Finalizado importação de registros!");
    }
}