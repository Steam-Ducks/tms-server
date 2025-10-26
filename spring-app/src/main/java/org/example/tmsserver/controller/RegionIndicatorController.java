package org.example.tmsserver.controller;

import org.example.tmsserver.dto.RegionIndicatorDTO;
import org.example.tmsserver.service.RegionIndicatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/indicators")
public class RegionIndicatorController {

    private final RegionIndicatorService regionIndicatorService;

    public RegionIndicatorController(RegionIndicatorService regionIndicatorService) {
        this.regionIndicatorService = regionIndicatorService;
    }

    @GetMapping("/daily")
    public ResponseEntity<Map<String, List<RegionIndicatorDTO>>> getIndicatorPerDay() {
        List<RegionIndicatorDTO> data = regionIndicatorService.getIndicatorPerDay();
        return ResponseEntity.ok(Map.of("daily", data));
    }

    @GetMapping("/hourly")
    public ResponseEntity<Map<String, List<RegionIndicatorDTO>>> getIndicatorPerHour() {
        List<RegionIndicatorDTO> data = regionIndicatorService.getIndicatorPerHour();
        return ResponseEntity.ok(Map.of("hourly", data));
    }

}
