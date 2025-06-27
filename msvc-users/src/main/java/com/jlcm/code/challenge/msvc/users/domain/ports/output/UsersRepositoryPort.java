package com.jlcm.code.challenge.msvc.users.domain.ports.output;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.jlcm.code.challenge.msvc.users.domain.entities.User;

public interface UsersRepositoryPort {
    Collection<User> findAll();

    Optional<User> findByUsername(String username);

    Optional<User> create(User user);

    Optional<User> update(String username, User user);

    Optional<User> delete(String username);

    Page<User> findAllPaginated(int page, int size, String sortBy, String sortDir);
}
