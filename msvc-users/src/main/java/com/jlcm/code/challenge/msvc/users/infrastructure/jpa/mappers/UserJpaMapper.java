package com.jlcm.code.challenge.msvc.users.infrastructure.jpa.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import com.jlcm.code.challenge.msvc.users.domain.entities.Gender;
import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.infrastructure.jpa.entities.UserJpaEntity;

@Mapper(componentModel = "spring")
@Component
public interface UserJpaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "name.title")
    @Mapping(target = "firstName", source = "name.firstName")
    @Mapping(target = "lastName", source = "name.lastName")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderToIndex")
    @Mapping(target = "country", source = "location.country")
    @Mapping(target = "state", source = "location.state")
    @Mapping(target = "city", source = "location.city")
    UserJpaEntity toJpaEntity(User user);

    @Mapping(target = "name", expression = "java(new FullName(userJpaEntity.getTitle(), userJpaEntity.getFirstName(), userJpaEntity.getLastName()))")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "indexToGender")
    @Mapping(target = "location", expression = "java(new Location(userJpaEntity.getCountry(), userJpaEntity.getState(), userJpaEntity.getCity()))")
    User toDomain(UserJpaEntity userJpaEntity);

    @Named("genderToIndex")
    public static Integer genderToIndex(Gender gender) {
        return gender.getIndex();
    }

    @Named("indexToGender")
    public static Gender indexToGender(Integer index) {
        if (index == null) {
            return null;
        }
        return Gender.fromIndex(index);
    }
}
