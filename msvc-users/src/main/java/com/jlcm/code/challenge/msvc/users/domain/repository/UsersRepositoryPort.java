package com.jlcm.code.challenge.msvc.users.domain.repository;

import java.util.Collection;
import java.util.Optional;

import com.jlcm.code.challenge.msvc.users.domain.User;

public interface UsersRepositoryPort {
    Collection<User> findAll();

    Optional<User> findByUsername(String username);

    Optional<User> create(User user);

    Optional<User> update(String username, User user);

    Optional<User> delete(String username);
}
