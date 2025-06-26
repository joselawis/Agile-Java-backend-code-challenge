package com.jlcm.code.challenge.msvc.users.application;

import java.util.Collection;
import java.util.Optional;

import com.jlcm.code.challenge.msvc.users.domain.User;
import com.jlcm.code.challenge.msvc.users.domain.dto.Country;

public interface UsersInteractionPort {

    public Collection<User> findAll();

    public Optional<User> findByUsername(String username);

    public Optional<User> create(User user);

    public Optional<User> update(String username, User user);

    public Optional<User> delete(String username);

    public Collection<User> generateUsers(int count);

    public Collection<Country> findAllSortedByLocation();

}
