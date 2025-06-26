package com.jlcm.code.challenge.msvc.users.infrastructure.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jlcm.code.challenge.msvc.users.infrastructure.jpa.entities.UserJpaEntity;

public interface UserJpaRepository extends CrudRepository<UserJpaEntity, Long> {

}
