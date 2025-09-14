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

    @Scheduled(cron = "0 */10 * * * *")
    public void execute() {
        speedRecordService.clearSpeedRecords();
    }
}