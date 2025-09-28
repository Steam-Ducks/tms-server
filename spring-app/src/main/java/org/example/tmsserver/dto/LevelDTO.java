package org.example.tmsserver.dto;

import org.example.tmsserver.entity.Region;

public record LevelDTO(Integer idLevel, Region region, Integer value) {
}
