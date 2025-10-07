package org.example.tmsserver.repository;

import org.example.tmsserver.entity.SpeedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SpeedRecordRepository extends JpaRepository<SpeedRecord, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM SpeedRecord")
    void deleteAllRecords();

    @Query("SELECT sr  FROM SpeedRecord sr JOIN sr.camera c JOIN c.region r WHERE r.idRegion = :regionId ")
    List<SpeedRecord> findAllByRegionId(@Param("regionId") Integer regionId);

    @Query("SELECT c.region.idRegion, c.idCamera, c.speedLimit, COUNT(sr), SUM(sr.speed)\n" + "FROM SpeedRecord sr\n" + "JOIN sr.camera c\n" + "WHERE c.region.idRegion IS NOT NULL AND c.speedLimit IS NOT NULL\n" + "GROUP BY c.region.idRegion, c.idCamera, c.speedLimit")
    List<Object[]> findRegionCameraAggregates();

    default void saveAllInBatches(List<SpeedRecord> records) {
        final int batchSize = 100;
        int totalBatches = (int) Math.ceil((double) records.size() / batchSize);

        for (int i = 0; i < records.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, records.size());
            List<SpeedRecord> batch = records.subList(i, endIndex);

            saveAll(batch);

            int currentBatch = (i / batchSize) + 1;
            System.out.println("Lote " + currentBatch + "/" + totalBatches + " salvo (" + batch.size() + " registros)");
        }
    }
}