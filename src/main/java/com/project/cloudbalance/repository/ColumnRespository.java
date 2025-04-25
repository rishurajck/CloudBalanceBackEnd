package com.project.cloudbalance.repository;

import com.project.cloudbalance.entity.Columns;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColumnRespository extends JpaRepository<Columns, Long> {
    Optional<Columns> findByDisplayName(String displayName);
}
