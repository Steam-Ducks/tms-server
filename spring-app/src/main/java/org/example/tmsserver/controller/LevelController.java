package org.example.tmsserver.controller;

import org.example.tmsserver.entity.Level;
import org.example.tmsserver.service.LevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/levels")
public class LevelController {

    private final LevelService levelService;

    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    @PostMapping("/{regionId}/calculate")
    public ResponseEntity<Level> calculate(@PathVariable Long regionId) {
        Level level = levelService.calculateLevelForRegion(regionId);
        return ResponseEntity.ok(level);
    }
}
