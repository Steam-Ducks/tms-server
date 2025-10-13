package org.example.tmsserver.repository;

import java.util.Optional;
import org.example.tmsserver.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByDescription(String description); // ex: "ROLE_USER"
}