package org.example.tmsserver.repository;

import java.util.Optional;// S
import org.example.tmsserver.entity.Region;//S  
import org.example.tmsserver.entity.Indicator;//S
import org.example.tmsserver.entity.RegionIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionIndicatorRepository extends JpaRepository<RegionIndicator, Long> {
    Optional<RegionIndicator> findTopByRegionAndIndicatorOrderByTimeDesc(Region region, Indicator indicator); //S

}
