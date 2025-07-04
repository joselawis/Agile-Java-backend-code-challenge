package com.jlcm.code.challenge.msvc.users.infrastructure.feign.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.jlcm.code.challenge.msvc.users.domain.entities.Gender;
import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.UserResultDto;

@Mapper(componentModel = "spring")
public interface RandomUserMapper {

    @Mapping(source = "name.first", target = "name.firstName")
    @Mapping(source = "name.last", target = "name.lastName")
    @Mapping(source = "picture.large", target = "pictureUrl")
    @Mapping(source = "login.username", target = "username")
    @Mapping(source = "gender", target = "gender", qualifiedByName = "labelToGender")
    User toDomain(UserResultDto userResultDto);

    @Named("labelToGender")
    public static Gender indexToGender(String label) {
        if (label == null || label.isEmpty()) {
            return null;
        }
        return Gender.fromLabel(label);
    }
}
