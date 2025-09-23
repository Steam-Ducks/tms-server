package org.example.tmsserver.repository;

import org.example.tmsserver.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

    @Query(value = "SELECT GET_REGION_BY_POINT(:lat, :lon) FROM dual", nativeQuery = true)
    Integer findRegionByPoint(@Param("lat") double lat, @Param("lon") double lon);

}
