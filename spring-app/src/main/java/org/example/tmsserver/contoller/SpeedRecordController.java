package org.example.tmsserver.contoller;

import org.example.tmsserver.service.SpeedRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpeedRecordController {

    private final SpeedRecordService speedRecordService;

    public SpeedRecordController(SpeedRecordService speedRecordService) {
        this.speedRecordService = speedRecordService;
    }

}
