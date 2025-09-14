package org.example.tmsserver.repository;

import org.example.tmsserver.entity.SpeedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SpeedRecordRepository extends JpaRepository<SpeedRecord, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM SpeedRecord")
    void deleteAllRecords();
}