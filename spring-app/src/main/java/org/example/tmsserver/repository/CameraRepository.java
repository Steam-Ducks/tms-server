package org.example.tmsserver.repository;

import org.example.tmsserver.entity.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CameraRepository extends JpaRepository<Camera, String> {

    @Query("SELECT c FROM Camera c WHERE UPPER(TRIM(c.idCamera)) IN :ids")
    List<Camera> findCamerasByIdList(@Param("ids") List<String> ids);
}
