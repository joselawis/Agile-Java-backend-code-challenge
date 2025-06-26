package com.jlcm.code.challenge.msvc.users.infrastructure.jpa.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.jlcm.code.challenge.msvc.users.infrastructure.jpa.entities.UserJpaEntity;

public interface UserJpaRepository extends CrudRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByUsername(String username);
}
