package org.example.tmsserver.repository;

import java.util.ArrayList;
import java.util.List;

import org.example.tmsserver.dto.RegionIndicatorDTO;
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
}
