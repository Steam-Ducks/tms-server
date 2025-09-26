package org.example.tmsserver.contoller;

import org.example.tmsserver.dto.ZoneLevelDTO;
import org.example.tmsserver.service.RegionIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/map" ) // URL base para todos os endpoints deste controller
public class MapController {

    private final RegionIndicatorService regionIndicatorService;

    @Autowired
    public MapController(RegionIndicatorService regionIndicatorService) {
        this.regionIndicatorService = regionIndicatorService;
    }

    @GetMapping("/zone-levels") // URL completa: /api/map/zone-levels
    public ResponseEntity<List<ZoneLevelDTO>> getZoneLevels() {
        List<ZoneLevelDTO> zoneLevels = regionIndicatorService.getZoneLevels();
        return ResponseEntity.ok(zoneLevels);
    }
}
