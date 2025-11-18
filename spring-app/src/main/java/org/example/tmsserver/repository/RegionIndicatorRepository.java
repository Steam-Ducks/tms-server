package org.example.tmsserver.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.tmsserver.dto.RegionIndicatorDTO;
import org.example.tmsserver.entity.Indicator;
import org.example.tmsserver.entity.RegionIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionIndicatorRepository extends JpaRepository<RegionIndicator, Integer> {
    @Query("SELECT r FROM RegionIndicator r WHERE r.region.idRegion = :regionId" +
        " ORDER BY r.time DESC LIMIT :limit")
    List<RegionIndicator> findValuesByRegion(@Param("regionId") Integer regionId, @Param("limit") Integer limit);

    @Query(
            value = """
        SELECT
            TO_CHAR(TRUNC(b.time), 'YYYY-MM-DD') AS day,
            r.name AS region_name,
            i.name AS indicator_name,
            ROUND(AVG(b.value), 2) AS average_value
        FROM region_indicator b
        JOIN region r ON r.id_region = b.id_region
        JOIN indicator i ON i.id_indicator = b.id_indicator
        WHERE b.time >= TRUNC(SYSDATE) - 14
        GROUP BY 
            TO_CHAR(TRUNC(b.time), 'YYYY-MM-DD'),
            r.name,
            i.name
        ORDER BY day, region_name, indicator_name
    """,
            nativeQuery = true)
    List<Object[]> findAverageIndicatorValuesPerDayRaw();

    @Query(
            value = """
        SELECT
            EXTRACT(HOUR FROM (FROM_TZ(CAST(b.time AS TIMESTAMP), 'UTC') AT TIME ZONE 'America/Sao_Paulo')) AS hour,
            r.name AS region_name,
            i.name AS indicator_name,
            ROUND(AVG(b.value), 2) AS average_value
        FROM region_indicator b
        JOIN region r ON r.id_region = b.id_region
        JOIN indicator i ON i.id_indicator = b.id_indicator
        WHERE b.time >= TRUNC(SYSDATE) - 14
        GROUP BY 
            EXTRACT(HOUR FROM (FROM_TZ(CAST(b.time AS TIMESTAMP), 'UTC') AT TIME ZONE 'America/Sao_Paulo')),
            r.name,
            i.name
        ORDER BY hour, region_name, indicator_name
    """,
            nativeQuery = true)
    List<Object[]> findAverageIndicatorValuesPerHourRaw();

    default List<RegionIndicatorDTO> mapToDTOPerDay() {
        List<Object[]> results = findAverageIndicatorValuesPerDayRaw();
        List<RegionIndicatorDTO> dtos = new ArrayList<>();

        for (Object[] row : results) {
            dtos.add(new RegionIndicatorDTO(
                    (String) row[0],
                    null,
                    (String) row[1],
                    (String) row[2],
                    ((Number) row[3]).doubleValue()
            ));
        }

        return dtos;
    }

    default List<RegionIndicatorDTO> mapToDTOPerHour() {
        List<Object[]> results = findAverageIndicatorValuesPerHourRaw();
        List<RegionIndicatorDTO> dtos = new ArrayList<>();

        for (Object[] row : results) {
            dtos.add(new RegionIndicatorDTO(
                    null,
                    ((Number) row[0]).intValue(),
                    (String) row[1],
                    (String) row[2],
                    ((Number) row[3]).doubleValue()
            ));
        }

        return dtos;
    }

    Optional<RegionIndicator> findTopByRegion_IdRegionAndIndicator_IdIndicatorOrderByTimeDesc(Integer regionId, Integer indicatorId);
  
    List<RegionIndicator> findByRegionIdRegionAndIndicatorOrderByTimeDesc(Integer regionId, Indicator indicator);

    @Query(value = """
        SELECT i.name, ri.change
        FROM region_indicator ri
        JOIN indicator i ON i.id_indicator = ri.id_indicator
        WHERE ri.id = (
            SELECT MAX(ri2.id)
            FROM region_indicator ri2
            WHERE ri2.id_indicator = ri.id_indicator
        )
        AND i.name != 'Weather'
        ORDER BY i.name
    """, nativeQuery = true)
    List<Object[]> findLatestIndicatorChanges();

    @Query("SELECT new org.example.tmsserver.dto.RegionIndicatorDTO(" +
            "TO_CHAR(ri.time, 'YYYY-MM-DD'), " +
            "null, " +
            "r.name, " +
            "i.name, " +
            "CAST(ri.value as double)) " +
            "FROM RegionIndicator ri " +
            "JOIN ri.region r " +
            "JOIN ri.indicator i " +
            "WHERE ri.time = (SELECT MAX(ri2.time) FROM RegionIndicator ri2 " +
            "WHERE ri2.region = ri.region AND ri2.indicator = ri.indicator) " +
            "AND r.name = :regionName " +
            "ORDER BY i.name")
    List<RegionIndicatorDTO> findLatestIndicatorsByRegion(@Param("regionName") String regionName);

    @Query("SELECT ri.change FROM RegionIndicator ri " +
            "JOIN ri.region r " +
            "JOIN ri.indicator i " +
            "WHERE ri.time = (SELECT MAX(ri2.time) FROM RegionIndicator ri2 " +
            "WHERE ri2.region = ri.region AND ri2.indicator = ri.indicator) " +
            "AND r.name = :regionName AND i.name = :indicatorName")
    String findLatestChangeByRegionAndIndicator(@Param("regionName") String regionName,
                                                @Param("indicatorName") String indicatorName);

    @Query("SELECT DISTINCT r.name FROM Region r")
    List<String> findAllRegionNames();
}


