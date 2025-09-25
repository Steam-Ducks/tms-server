package org.example.tmsserver.repository;

import java.util.List;
import org.example.tmsserver.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

    @Query(value = "SELECT GET_REGION_BY_POINT(:lat, :lon) FROM dual", nativeQuery = true)
    Integer findRegionByPoint(@Param("lat") BigDecimal lat, @Param("lon") BigDecimal lon);

    @Query("select i.value from RegionIndicator i where i.region.id = :regionId")
    List<Integer> findValuesByRegion(@Param("regionId") Integer regionId);
}

