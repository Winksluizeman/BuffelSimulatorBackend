package org.example.buffelsimulatorbackend.application.port;

import org.example.buffelsimulatorbackend.domain.UserModel;
import java.util.Optional;

public interface IUserRepository {
    Optional<UserModel> findByUsername(String username);
    void save (UserModel model);

    void flush();
}