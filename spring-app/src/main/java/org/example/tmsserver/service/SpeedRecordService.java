package org.example.tmsserver.service;

import org.example.tmsserver.repository.SpeedRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class SpeedRecordService {

    private final SpeedRecordRepository repository;

    public SpeedRecordService(SpeedRecordRepository repository) {
        this.repository = repository;
    }

    public void clearSpeedRecords() {
        repository.deleteAllRecords();
    }
}