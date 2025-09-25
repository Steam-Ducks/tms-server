package org.example.tmsserver.service;

import org.example.tmsserver.entity.Region;
import org.example.tmsserver.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public Region getRegionByPoint(BigDecimal lat, BigDecimal lon) {
        Integer regionId = regionRepository.findRegionByPoint(lat, lon);
        if (regionId != null) {
            return regionRepository.findById(regionId).orElse(null);
        }
        return null;
    }
}
