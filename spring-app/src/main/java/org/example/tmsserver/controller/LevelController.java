package org.example.tmsserver.controller;

import org.example.tmsserver.dto.LevelDTO;
import org.example.tmsserver.entity.Level;
import org.example.tmsserver.service.LevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/levels")
public class LevelController {

    private final LevelService levelService;

    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    @PostMapping("/{regionId}/calculate")
    public ResponseEntity<LevelDTO> calculate(@PathVariable("regionId") Integer regionId) {
        Level level = levelService.calculateLevelForRegion(regionId);
        
        LevelDTO dto = new LevelDTO(
            level.getIdLevel(),
            level.getRegion(),
            level.getValue()           
        );
        return ResponseEntity.ok(dto);
    }
}
