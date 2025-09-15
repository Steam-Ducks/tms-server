package org.example.tmsserver.job;

import org.example.tmsserver.service.SpeedRecordService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SpeedDataImportJob {

    private final SpeedRecordService speedRecordService;

    public SpeedDataImportJob(SpeedRecordService speedRecordService) {
        this.speedRecordService = speedRecordService;
    }

    //Para teste, remova 0 /10 e deixe assim cron = "0 * * * * *" para repetir a cada minuto
    @Scheduled(cron = "0 */10 * * * *")
    public void execute() {
        speedRecordService.clearSpeedRecords();
        speedRecordService.fetchAndSaveSpeedRecords();
        System.out.println("Speed records atualizados!");
    }
}