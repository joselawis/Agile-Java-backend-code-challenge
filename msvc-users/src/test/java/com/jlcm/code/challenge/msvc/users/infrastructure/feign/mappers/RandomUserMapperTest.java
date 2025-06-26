package com.jlcm.code.challenge.msvc.users.infrastructure.feign.mappers;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.jlcm.code.challenge.msvc.users.domain.entities.Gender;
import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.LocationDto;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.LoginDto;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.NameDto;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.PictureDto;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.UserResultDto;

@RunWith(MockitoJUnitRunner.class)
class RandomUserMapperTest {

    private RandomUserMapper mapper = new RandomUserMapperImpl();

    @Test
    void shouldMapUserResultDtoToDomain() {
        // Given a UserResultDto with all fields populated
        UserResultDto userResultDto = UserResultDto.builder()
                .name(new NameDto("Mr", "John", "Doe"))
                .login(new LoginDto("jdoe"))
                .picture(new PictureDto("http://img.com/pic.jpg"))
                .email("john@example.com")
                .gender("male")
                .location(new LocationDto("LA", "California", "USA"))
                .build();

        User user = mapper.toDomain(userResultDto);

        assertEquals("jdoe", user.getUsername());
        assertEquals("Mr", user.getName().getTitle());
        assertEquals("John", user.getName().getFirstName());
        assertEquals("Doe", user.getName().getLastName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals(Gender.MALE, user.getGender());
        assertEquals("http://img.com/pic.jpg", user.getPictureUrl());
        assertEquals("USA", user.getLocation().getCountry());
        assertEquals("California", user.getLocation().getState());
        assertEquals("LA", user.getLocation().getCity());
    }
}
