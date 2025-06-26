package com.jlcm.code.challenge.msvc.users.infrastructure.feign.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.UserResultDto;

@Mapper(componentModel = "spring")
@Component
public interface RandomUserMapper {

    @Mapping(source = "name.first", target = "name.firstName")
    @Mapping(source = "name.last", target = "name.lastName")
    @Mapping(source = "picture.large", target = "pictureUrl")
    @Mapping(source = "login.username", target = "username")
    User toDomain(UserResultDto userResultDto);
}
