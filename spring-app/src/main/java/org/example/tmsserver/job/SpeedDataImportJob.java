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
    @Scheduled(cron = "0 */10 * * * *")
    public void execute() {
        System.out.println("Inicializado importação de registros!");

        speedRecordService.clearSpeedRecords();
        speedRecordService.fetchAndSaveSpeedRecords();
        regionIndicatorService.calculateAndSaveRegionIndicators();

        System.out.println("Finalizado importação de registros!");
    }
}