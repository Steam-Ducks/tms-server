package org.example.tmsserver.controller;

import org.example.tmsserver.dto.LevelDTO;
import org.example.tmsserver.entity.Level;
import org.example.tmsserver.service.LevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/levels")
public class LevelController {

    private final LevelService levelService;

    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    @GetMapping("/city")
    public ResponseEntity<Map<String, Integer>> getCityLevels() {
        Integer level = levelService.getCityLevel();
        return ResponseEntity.ok(Map.of("level", level));
    }
}