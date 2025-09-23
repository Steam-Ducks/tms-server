package org.example.tmsserver.repository;

import org.example.tmsserver.entity.RegionIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionIndicatorRepository extends JpaRepository<RegionIndicator, Integer> {
}
