package com.jlcm.code.challenge.msvc.users.application;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;

import com.jlcm.code.challenge.msvc.users.domain.User;

public interface UsersInteractionPort {

    public Collection<User> findAll();

    public Optional<User> findByUsername(String username);

    public User create(User user);

    public Optional<User> update(String username, User user);

    public Optional<User> delete(String username);

    public Collection<User> generateUsers(int count);

    // TODO: Think about a better way to handle this
    public SortedMap<String, List<User>> findAllSortedByLocation();

}
