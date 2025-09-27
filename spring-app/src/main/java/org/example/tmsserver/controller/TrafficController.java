package org.example.tmsserver.controller;

import org.example.tmsserver.dto.ZoneLevelDTO;
import org.example.tmsserver.service.LevelService;
import org.example.tmsserver.service.RegionIndicatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/levels")
@CrossOrigin
public class TrafficController {

    private final LevelService levelService;

    public TrafficController(LevelService levelService) {
        this.levelService = levelService;
    }

    @GetMapping("/zones")
    public ResponseEntity<List<ZoneLevelDTO>> getMapLevels() {

        List<ZoneLevelDTO> zoneLevels = levelService.getLatestRegionLevels();
        return ResponseEntity.ok(zoneLevels);
    }

}