package com.jlcm.code.challenge.msvc.users.domain.ports.input;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.jlcm.code.challenge.msvc.users.domain.dto.Country;
import com.jlcm.code.challenge.msvc.users.domain.entities.User;

public interface UsersInteractionPort {

    public Collection<User> findAll();

    public Optional<User> findByUsername(String username);

    public Optional<User> create(User user);

    public Optional<User> update(String username, User user);

    public Optional<User> delete(String username);

    public Collection<User> generateUsers(int count);

    public Collection<Country> findAllSortedByLocation();

    public Page<User> findAllPaginated(int page, int size, String sortBy, String sortDir);

}
