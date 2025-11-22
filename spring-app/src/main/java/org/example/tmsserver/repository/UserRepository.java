package org.example.tmsserver.repository;

import org.example.tmsserver.entity.Role;
import org.example.tmsserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Mantenha esta importação se for necessária para findByUsername

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByRole(Role role);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByTelegramId(Integer telegramId);

}