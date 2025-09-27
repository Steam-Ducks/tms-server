package org.example.tmsserver.repository;

import org.example.tmsserver.entity.AppLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppLevelRepository extends JpaRepository<AppLevel, Long> {
    List<AppLevel> findTop6ByOrderByTimeDesc();
}