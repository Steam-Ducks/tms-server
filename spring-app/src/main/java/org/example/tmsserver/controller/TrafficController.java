package org.example.tmsserver.controller;

import org.example.tmsserver.dto.ZoneLevelDTO;
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
public class TrafficController {

    private final RegionIndicatorService regionIndicatorService;

    public TrafficController(RegionIndicatorService regionIndicatorService) {
        this.regionIndicatorService = regionIndicatorService;
    }

    @GetMapping("/levels") // s
    public ResponseEntity<List<ZoneLevelDTO>> getMapLevels() {

        List<ZoneLevelDTO> zoneLevels = regionIndicatorService.getLatestRegionLevels();
        return ResponseEntity.ok(zoneLevels);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/zones")
    public List<Map<String, Object>> getZoneLevels() {
        List<Map<String, Object>> zoneLevels = List.of(
                Map.of("id", "1", "name", "Zona Sul", "level", 1),
                Map.of("id", "2", "name", "Zona Sudeste", "level", 2),
                Map.of("id", "3", "name", "Zona Leste", "level", 3),
                Map.of("id", "4", "name", "Zona Central", "level", 4),
                Map.of("id", "5", "name", "Zona Oeste", "level", 5),
                Map.of("id", "6", "name", "Zona Norte", "level", 5)
        );
        return zoneLevels;
    }
}