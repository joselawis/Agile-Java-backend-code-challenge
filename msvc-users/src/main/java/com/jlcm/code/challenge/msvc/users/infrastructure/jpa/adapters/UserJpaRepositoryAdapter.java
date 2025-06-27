package com.jlcm.code.challenge.msvc.users.infrastructure.jpa.adapters;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.domain.ports.output.UsersRepositoryPort;
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
    public Optional<User> create(User user) {
        UserJpaEntity newUser = userJpaRepository.save(userJpaMapper.toJpaEntity(user));
        return Optional.of(userJpaMapper.toDomain(newUser));
    }

    @Override
    public Optional<User> update(String username, User user) {
        Optional<UserJpaEntity> existingUser = userJpaRepository.findByUsername(username);
        if (existingUser.isEmpty()) {
            return Optional.empty();
        }

        UserJpaEntity updatedUser = userJpaMapper.toJpaEntity(user);
        updatedUser.setId(existingUser.get().getId());
        UserJpaEntity savedUser = userJpaRepository.save(updatedUser);
        return Optional.of(userJpaMapper.toDomain(savedUser));
    }

    @Override
    public Optional<User> delete(String username) {
        Optional<UserJpaEntity> existingUser = userJpaRepository.findByUsername(username);
        if (existingUser.isEmpty()) {
            return Optional.empty();
        }
        try {
            userJpaRepository.delete(existingUser.get());
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(userJpaMapper.toDomain(existingUser.get()));
    }

    @Override
    public Page<User> findAllPaginated(int page, int size, String sortBy, String sortDir) {
        PageRequest pageable = PageRequest.of(page,
                size,
                Sort.by("desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC,
                        sortBy));
        Page<UserJpaEntity> allUsersPaginated = userJpaRepository.findAll(pageable);
        return allUsersPaginated.map(userJpaMapper::toDomain);
    }

}
