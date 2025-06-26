package com.jlcm.code.challenge.msvc.users.infrastructure.jpa.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.jlcm.code.challenge.msvc.users.domain.entities.FullName;
import com.jlcm.code.challenge.msvc.users.domain.entities.Gender;
import com.jlcm.code.challenge.msvc.users.domain.entities.Location;
import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.infrastructure.jpa.entities.UserJpaEntity;

@RunWith(MockitoJUnitRunner.class)
class UserJpaMapperTest {

    private UserJpaMapper mapper = new UserJpaMapperImpl();

    @Test
    void shouldMapUserToJpaEntity() {
        // given
        User user = User.builder()
                .username("jdoe")
                .name(new FullName("Mr", "John", "Doe"))
                .email("john@example.com")
                .gender(Gender.MALE)
                .pictureUrl("http://img.com/pic.jpg")
                .location(new Location("USA", "California", "LA"))
                .build();

        // when
        UserJpaEntity entity = mapper.toJpaEntity(user);

        // then
        assertEquals("jdoe", entity.getUsername());
        assertEquals("Mr", entity.getTitle());
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals("john@example.com", entity.getEmail());
        assertEquals(0, entity.getGender());
        assertEquals("http://img.com/pic.jpg", entity.getPictureUrl());
        assertEquals("USA", entity.getCountry());
        assertEquals("California", entity.getState());
        assertEquals("LA", entity.getCity());
    }

    @Test
    void shouldMapJpaEntityToUser() {
        // given
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

        // when
        User user = mapper.toDomain(entity);

        // then
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
