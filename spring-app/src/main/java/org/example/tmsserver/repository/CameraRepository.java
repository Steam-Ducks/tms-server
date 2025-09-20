package org.example.tmsserver.repository;

import org.example.tmsserver.entity.Camera;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CameraRepository extends JpaRepository<Camera, String> {
}
