package com.jlcm.code.challenge.msvc.users.infrastructure.jpa.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jlcm.code.challenge.msvc.users.domain.entities.FullName;
import com.jlcm.code.challenge.msvc.users.domain.entities.Gender;
import com.jlcm.code.challenge.msvc.users.domain.entities.Location;
import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.infrastructure.jpa.entities.UserJpaEntity;

@ExtendWith(MockitoExtension.class)
class UserJpaMapperTest {

    private final UserJpaMapper mapper = new UserJpaMapperImpl();

    @Test
    void shouldMapUserToJpaEntity() {
        // Given
        User user = User.builder()
                .username("jdoe")
                .name(new FullName("Mr", "John", "Doe"))
                .email("john@example.com")
                .gender(Gender.MALE)
                .pictureUrl("http://img.com/pic.jpg")
                .location(new Location("USA", "California", "LA"))
                .build();

        // When
        UserJpaEntity entity = mapper.toJpaEntity(user);

        // Then
        assertThat(entity.getUsername()).isEqualTo("jdoe");
        assertThat(entity.getTitle()).isEqualTo("Mr");
        assertThat(entity.getFirstName()).isEqualTo("John");
        assertThat(entity.getLastName()).isEqualTo("Doe");
        assertThat(entity.getEmail()).isEqualTo("john@example.com");
        assertThat(entity.getGender()).isZero();
        assertThat(entity.getPictureUrl()).isEqualTo("http://img.com/pic.jpg");
        assertThat(entity.getCountry()).isEqualTo("USA");
        assertThat(entity.getState()).isEqualTo("California");
        assertThat(entity.getCity()).isEqualTo("LA");
    }

    @Test
    void shouldMapJpaEntityToUser() {
        // Given
        UserJpaEntity entity = UserJpaEntity.builder()
                .username("jdoe")
                .title("Mr")
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .gender(0)
                .pictureUrl("http://img.com/pic.jpg")
                .country("USA")
                .state("California")
                .city("LA")
                .build();

        // When
        User user = mapper.toDomain(entity);

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
