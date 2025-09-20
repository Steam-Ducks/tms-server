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
public interface SpeedRecordRepository extends JpaRepository<SpeedRecord, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM SpeedRecord")
    void deleteAllRecords();

    @Query("SELECT sr  FROM SpeedRecord sr JOIN sr.camera c JOIN c.region r WHERE r.idRegion = :regionId ")
    List<SpeedRecord> findAllByRegionId(@Param("regionId") Long regionId);
}