package org.example.tmsserver.controller;

import org.example.tmsserver.dto.WorstStreetByRegionDTO;
import org.example.tmsserver.service.LevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/streets")

public class StreetController {

    private final LevelService levelService;

    public StreetController(LevelService levelService) {
        this.levelService = levelService;
    }

    @GetMapping("/worst")
    public ResponseEntity<List<WorstStreetByRegionDTO>> getWorstStreets() {
        return ResponseEntity.ok(levelService.getWorstStreetsByRegion());
    }
}
