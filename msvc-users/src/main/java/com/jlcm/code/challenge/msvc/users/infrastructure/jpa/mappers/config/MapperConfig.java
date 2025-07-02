package com.jlcm.code.challenge.msvc.users.infrastructure.jpa.mappers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jlcm.code.challenge.msvc.users.infrastructure.jpa.mappers.UserJpaMapper;
import com.jlcm.code.challenge.msvc.users.infrastructure.jpa.mappers.UserJpaMapperImpl;

@Configuration
public class MapperConfig {

    @Bean
    UserJpaMapper userJpaMapper() {
        return new UserJpaMapperImpl();
    }
}
