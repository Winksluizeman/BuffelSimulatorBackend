package org.example.buffelsimulatorbackend.data.repository;

import org.example.buffelsimulatorbackend.data.table.WeightTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeightJpaRepository extends JpaRepository<WeightTable, Long> {
}