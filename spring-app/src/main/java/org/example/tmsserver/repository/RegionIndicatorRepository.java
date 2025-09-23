package org.example.tmsserver.repository;

import java.util.List;

import org.example.tmsserver.entity.RegionIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionIndicatorRepository extends JpaRepository<RegionIndicator, Integer> {
    @Query("SELECT r.value FROM RegionIndicator r WHERE r.idRegion = :idRegion")
    List<Integer> findValuesByRegion(@Param("idRegion") Long idRegion);
}

