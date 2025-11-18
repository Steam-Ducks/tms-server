package org.example.tmsserver.controller;

import org.example.tmsserver.dto.RegionIndicatorDTO;
import org.example.tmsserver.service.RegionIndicatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getIndicatorStatus() {
        Map<String, String> data = regionIndicatorService.getIndicatorStatus();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/region/{regionName}")
    public ResponseEntity<Map<String, Object>> getIndicatorsByRegion(@PathVariable String regionName) {
        try {
            Map<String, Object> data = regionIndicatorService.getIndicatorsByRegion(regionName);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao buscar indicadores da região: " + e.getMessage()));
        }
    }

    @GetMapping("/regions")
    public ResponseEntity<Map<String, Object>> getAllRegionsIndicators() {
        try {
            Map<String, Object> data = regionIndicatorService.getAllRegionsWithIndicators();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao buscar indicadores: " + e.getMessage()));
        }
    }

    @GetMapping("/regions/summary")
    public ResponseEntity<List<Map<String, String>>> getRegionsSummary() {
        try {
            List<Map<String, String>> summary = regionIndicatorService.getRegionsSummary();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
