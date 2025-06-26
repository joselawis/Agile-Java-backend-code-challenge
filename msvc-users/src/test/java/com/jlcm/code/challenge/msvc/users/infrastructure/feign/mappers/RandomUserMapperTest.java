package com.jlcm.code.challenge.msvc.users.infrastructure.feign.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jlcm.code.challenge.msvc.users.domain.entities.Gender;
import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.LocationDto;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.LoginDto;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.NameDto;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.PictureDto;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.UserResultDto;

@ExtendWith(MockitoExtension.class)
class RandomUserMapperTest {

    private final RandomUserMapper mapper = new RandomUserMapperImpl();

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

        // When
        User user = mapper.toDomain(userResultDto);

        // Then
        assertThat(user.getUsername()).isEqualTo("jdoe");
        assertThat(user.getName().getTitle()).isEqualTo("Mr");
        assertThat(user.getName().getFirstName()).isEqualTo("John");
        assertThat(user.getName().getLastName()).isEqualTo("Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
        assertThat(user.getGender()).isEqualTo(Gender.MALE);
        assertThat(user.getPictureUrl()).isEqualTo("http://img.com/pic.jpg");
        assertThat(user.getLocation().getCountry()).isEqualTo("USA");
        assertThat(user.getLocation().getState()).isEqualTo("California");
        assertThat(user.getLocation().getCity()).isEqualTo("LA");
    }
}
