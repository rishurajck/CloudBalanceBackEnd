package com.project.cloudbalance.repository;

import com.project.cloudbalance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    @Query("SELECT u FROM User u WHERE UPPER(u.role) = 'CUSTOMER'")
    List<User> findByRoleIgnoreCase();

}
