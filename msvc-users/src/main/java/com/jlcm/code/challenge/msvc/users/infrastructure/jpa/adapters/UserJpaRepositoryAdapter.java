package com.jlcm.code.challenge.msvc.users.infrastructure.jpa.adapters;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jlcm.code.challenge.msvc.users.domain.User;
import com.jlcm.code.challenge.msvc.users.domain.repository.UsersRepositoryPort;
import com.jlcm.code.challenge.msvc.users.infrastructure.jpa.entities.UserJpaEntity;
import com.jlcm.code.challenge.msvc.users.infrastructure.jpa.mappers.UserJpaMapper;
import com.jlcm.code.challenge.msvc.users.infrastructure.jpa.repositories.UserJpaRepository;

@Service
public class UserJpaRepositoryAdapter implements UsersRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapper userJpaMapper;

    public UserJpaRepositoryAdapter(UserJpaRepository userJpaRepository, UserJpaMapper userJpaMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userJpaMapper = userJpaMapper;
    }

    @Override
    public Collection<User> findAll() {
        return ((Collection<UserJpaEntity>) userJpaRepository.findAll()).stream()
                .map(userJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(userJpaMapper::toDomain);
    }

    @Override
    public Optional<User> save(User user) {
        UserJpaEntity newUser = userJpaRepository.save(userJpaMapper.toJpaEntity(user));
        return Optional.of(userJpaMapper.toDomain(newUser));
    }

    @Override
    public Optional<User> delete(User user) {
        try {
            userJpaRepository.delete(userJpaMapper.toJpaEntity(user));
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

}
