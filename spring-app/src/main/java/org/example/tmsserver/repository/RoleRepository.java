package org.example.tmsserver.repository;

import org.example.tmsserver.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByDescription(String description);
    boolean existsByDescription(String description);
}
