package org.example.buffelsimulatorbackend.data.repository;

import org.example.buffelsimulatorbackend.application.port.IUserRepository;
import org.example.buffelsimulatorbackend.data.table.UserTable;
import org.example.buffelsimulatorbackend.domain.UserModel;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class UserRepository implements IUserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepository(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(u -> new UserModel(u.getId(), u.getUsername(), u.getEmail(), u.getPassword(), u.getCreatedAt()));
    }

    @Override
    public void save (UserModel user) {
        UserTable table = new UserTable();
        table.setUsername(user.getUsername());
        table.setPassword(user.getPassword());
        table.setEmail(user.getEmail());
        table.setCreatedAt(user.getCreatedAt());
        userJpaRepository.save(table);
    }
}