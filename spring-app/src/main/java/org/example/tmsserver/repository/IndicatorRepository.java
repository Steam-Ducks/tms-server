package org.example.tmsserver.repository;

import org.example.tmsserver.entity.Indicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IndicatorRepository extends JpaRepository<Indicator, Integer> {
    @Query("SELECT i FROM Indicator i WHERE i.name = :name")
    Optional<Indicator> findByName(@Param("name") String name);
}
