package com.jlcm.code.challenge.msvc.users.infrastructure.jpa.adapters;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jlcm.code.challenge.msvc.users.domain.User;
import com.jlcm.code.challenge.msvc.users.domain.repository.UsersRepositoryPort;

@Service
public class UserJpaRepositoryAdapter implements UsersRepositoryPort {

    @Override
    public Collection<User> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<User> findByUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
    }

    @Override
    public Optional<User> create(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public Optional<User> update(String username, User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Optional<User> delete(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
