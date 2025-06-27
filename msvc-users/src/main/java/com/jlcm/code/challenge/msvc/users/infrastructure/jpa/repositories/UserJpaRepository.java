package com.jlcm.code.challenge.msvc.users.infrastructure.jpa.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jlcm.code.challenge.msvc.users.infrastructure.jpa.entities.UserJpaEntity;

public interface UserJpaRepository
        extends PagingAndSortingRepository<UserJpaEntity, Long>, CrudRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByUsername(String username);
}
