package com.jlcm.code.challenge.msvc.users.domain.repository;

import java.util.Collection;
import java.util.Optional;

import com.jlcm.code.challenge.msvc.users.domain.entities.User;

public interface UsersRepositoryPort {
    Collection<User> findAll();

    Optional<User> findByUsername(String username);

    Optional<User> save(User user);

    Optional<User> delete(User user);
}
