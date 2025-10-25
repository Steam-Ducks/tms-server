package org.example.tmsserver.repository;

import org.example.tmsserver.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByDescription(String description);
    boolean existsByDescription(String description);

    @Query("SELECT r FROM Role r JOIN r.regions reg WHERE reg.idRegion = :regionId")
    List<Role> findByRegionsContaining(@Param("regionId") Integer regionId);

}