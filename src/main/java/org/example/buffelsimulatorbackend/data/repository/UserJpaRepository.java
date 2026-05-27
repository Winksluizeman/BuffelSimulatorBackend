// data/repository/UserJpaRepository.java
package org.example.buffelsimulatorbackend.data.repository;

import org.example.buffelsimulatorbackend.data.table.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserTable, Long> {
    Optional<UserTable> findByName(String name);
}