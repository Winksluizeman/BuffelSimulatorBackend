package org.example.buffelsimulatorbackend.data.repository;

import org.example.buffelsimulatorbackend.data.table.WeightTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeightJpaRepository extends JpaRepository<WeightTable, Long> {
    List<WeightTable> findAllByUsername(String username);
}