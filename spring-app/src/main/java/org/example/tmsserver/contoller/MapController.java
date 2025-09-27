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
@RequestMapping("/api/map" ) // URL base para os endpoints de mapa
public class MapController {

    private final RegionIndicatorService regionIndicatorService;

    @Autowired
    public MapController(RegionIndicatorService regionIndicatorService) {
        this.regionIndicatorService = regionIndicatorService;
    }

    @GetMapping("/levels") // s
    public ResponseEntity<List<ZoneLevelDTO>> getMapLevels() {
    
        List<ZoneLevelDTO> zoneLevels = regionIndicatorService.getLatestRegionLevels();
        return ResponseEntity.ok(zoneLevels);
    }
}
