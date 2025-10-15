package org.example.tmsserver.repository;

import org.example.tmsserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Mantenha esta importação se for necessária para findByUsername

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Métodos da branch dev
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}