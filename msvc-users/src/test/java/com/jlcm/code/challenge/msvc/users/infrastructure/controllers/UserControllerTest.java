package com.jlcm.code.challenge.msvc.users.infrastructure.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.jlcm.code.challenge.msvc.users.domain.dto.City;
import com.jlcm.code.challenge.msvc.users.domain.dto.Country;
import com.jlcm.code.challenge.msvc.users.domain.dto.State;
import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.domain.ports.input.UsersInteractionPort;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UsersInteractionPort usersInteractionPort;

    @InjectMocks
    private UserController userController;

    @Test
    void getAll_shouldReturnNoContent_whenNoUsersExist() {
        // Given
        when(usersInteractionPort.findAll()).thenReturn(List.of());

        // When
        ResponseEntity<List<User>> response = userController.getAll();

        // Then
        assertThat(response).isEqualTo(ResponseEntity.noContent().build());
        assertThat(response.getBody()).isNull();
        verify(usersInteractionPort).findAll();
    }

    @Test
    void getAll_shouldReturnUsers_whenUsersExist() {
        // Given
        User user1 = User.builder().username("user1").build();
        User user2 = User.builder().username("user2").build();
        when(usersInteractionPort.findAll()).thenReturn(List.of(user1, user2));

        // When
        ResponseEntity<List<User>> response = userController.getAll();

        // Then
        assertThat(response).isEqualTo(ResponseEntity.ok(List.of(user1, user2)));
        assertThat(response.getBody())
                .isNotNull()
                .hasSize(2);
        verify(usersInteractionPort).findAll();
    }

    @Test
    void getByUsername_shouldReturnUser_whenUserExists() {
        // Given
        String username = "user1";
        User user = User.builder().username(username).build();
        when(usersInteractionPort.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        ResponseEntity<User> response = userController.getByUsername(username);

        // Then
        assertThat(response).isEqualTo(ResponseEntity.ok(user));
        assertThat(response.getBody())
                .isNotNull()
                .extracting(User::getUsername)
                .isEqualTo(username);
        verify(usersInteractionPort).findByUsername(username);
    }

    @Test
    void getByUsername_shouldReturnNotFound_whenUserDoesNotExist() {
        // Given
        String username = "user1";
        when(usersInteractionPort.findByUsername(username)).thenReturn(Optional.empty());

        // When
        ResponseEntity<User> response = userController.getByUsername(username);

        // Then
        assertThat(response).isEqualTo(ResponseEntity.notFound().build());
        assertThat(response.getBody()).isNull();
        verify(usersInteractionPort).findByUsername(username);
    }

    @Test
    void create_shouldReturnCreatedUser() {
        // Given
        String username = "newUser";
        User newUser = User.builder().username(username).build();
        when(usersInteractionPort.create(newUser)).thenReturn(Optional.of(newUser));

        // When
        ResponseEntity<User> response = userController.create(newUser);

        // Then
        assertThat(response).isEqualTo(ResponseEntity.ok(newUser));
        assertThat(response.getBody())
                .isNotNull()
                .extracting(User::getUsername)
                .isEqualTo(username);
        verify(usersInteractionPort).create(newUser);
    }

    @ParameterizedTest(name = "create should return bad request when: {0}")
    @MethodSource("invalidCreateScenarios")
    void create_shouldReturnBadRequest_whenInvalidScenarios(String testCase, User user, boolean shouldCallService) {
        // Given
        if (shouldCallService && user != null) {
            when(usersInteractionPort.create(user)).thenReturn(Optional.empty());
        }

        // When
        ResponseEntity<User> response = userController.create(user);

        // Then
        assertThat(response).isEqualTo(ResponseEntity.badRequest().build());
        assertThat(response.getBody()).isNull();

        if (shouldCallService && user != null) {
            verify(usersInteractionPort).create(user);
        }
    }

    private static Stream<Arguments> invalidCreateScenarios() {
        return Stream.of(
                Arguments.of("user is null", null, false),
                Arguments.of("username is empty", User.builder().username("").build(), false),
                Arguments.of("user already exists", User.builder().username("existingUser").build(), true));
    }

    @Test
    void update_shouldReturnUpdatedUser_whenUserExists() {
        // Given
        String username = "existingUser";
        User updatedUser = User.builder().username(username).build();
        when(usersInteractionPort.update(eq(username), any(User.class))).thenReturn(Optional.of(updatedUser));

        // When
        ResponseEntity<User> response = userController.update(username, updatedUser);

        // Then
        assertThat(response).isEqualTo(ResponseEntity.ok(updatedUser));
        assertThat(response.getBody())
                .isNotNull()
                .extracting(User::getUsername)
                .isEqualTo(username);
        verify(usersInteractionPort).update(eq(username), any(User.class));
    }

    @Test
    void update_shouldReturnNotFound_whenUserDoesNotExist() {
        // Given
        String username = "nonExistingUser";
        User userData = User.builder().username(username).build();
        when(usersInteractionPort.update(eq(username), any(User.class))).thenReturn(Optional.empty());

        // When
        ResponseEntity<User> response = userController.update(username, userData);

        // Then
        assertThat(response).isEqualTo(ResponseEntity.notFound().build());
        assertThat(response.getBody()).isNull();
        verify(usersInteractionPort).update(eq(username), any(User.class));
    }

    @Test
    void update_shouldReturnBadRequest_whenUserDataIsInvalid() {
        // Given
        String username = "invalidUser";
        User invalidUser = User.builder().username("").build();

        // When
        ResponseEntity<User> response = userController.update(username, invalidUser);

        // Then
        assertThat(response).isEqualTo(ResponseEntity.badRequest().build());
        assertThat(response.getBody()).isNull();
    }

    @Test
    void delete_shouldReturnDeletedUser_whenUserExists() {
        // Given
        String username = "userToDelete";
        User deletedUser = User.builder().username(username).build();
        when(usersInteractionPort.delete(username)).thenReturn(Optional.of(deletedUser));

        // When
        ResponseEntity<User> response = userController.delete(username);

        // Then
        assertThat(response).isEqualTo(ResponseEntity.ok(deletedUser));
        assertThat(response.getBody())
                .isNotNull()
                .extracting(User::getUsername)
                .isEqualTo(username);
        verify(usersInteractionPort).delete(username);
    }

    @Test
    void delete_shouldReturnNotFound_whenUserDoesNotExist() {
        // Given
        String username = "nonExistingUser";
        when(usersInteractionPort.delete(username)).thenReturn(Optional.empty());

        // When
        ResponseEntity<User> response = userController.delete(username);

        // Then
        assertThat(response).isEqualTo(ResponseEntity.notFound().build());
        assertThat(response.getBody()).isNull();
        verify(usersInteractionPort).delete(username);
    }

    @Test
    void generate_shouldReturnGeneratedUsers() {
        // Given
        int count = 5;
        List<User> generatedUsers = List.of(
                User.builder().username("user1").build(),
                User.builder().username("user2").build());
        when(usersInteractionPort.generateUsers(count)).thenReturn(generatedUsers);

        // When
        ResponseEntity<List<User>> response = userController.generate(count);

        // Then
        assertThat(response).isEqualTo(ResponseEntity.ok(generatedUsers));
        assertThat(response.getBody())
                .isNotNull()
                .hasSize(2);
        verify(usersInteractionPort).generateUsers(count);
    }

    @Test
    void generate_shouldReturnNoContent_whenNoUsersGenerated() {
        // Given
        int count = 0;
        when(usersInteractionPort.generateUsers(count)).thenReturn(List.of());

        // When
        ResponseEntity<List<User>> response = userController.generate(count);

        // Then
        assertThat(response).isEqualTo(ResponseEntity.noContent().build());
        assertThat(response.getBody()).isNull();
        verify(usersInteractionPort).generateUsers(count);
    }

    @Test
    void getAllSortedByLocation_shouldReturnUsersSortedByLocation() {
        // Given
        User user1 = User.builder().username("user1").build();
        User user2 = User.builder().username("user2").build();

        City city1 = new City("New York", 1, List.of(user1));
        City city2 = new City("Los Angeles", 1, List.of(user2));

        State state1 = new State("New York", 1, List.of(city1));
        State state2 = new State("California", 1, List.of(city2));

        Country country = new Country("USA", 2, List.of(state1, state2));

        List<Country> sortedUsers = List.of(country);
        when(usersInteractionPort.findAllSortedByLocation()).thenReturn(sortedUsers);

        // When
        ResponseEntity<List<Country>> response = userController.getAllSortedByLocation();

        // Then
        assertThat(response).isEqualTo(ResponseEntity.ok(sortedUsers));
        assertThat(response.getBody())
                .isNotNull()
                .hasSize(1)
                .first()
                .satisfies(responseCountry -> {
                    assertThat(responseCountry.getName()).isEqualTo("USA");
                    assertThat(responseCountry.getNumberOfUsers()).isEqualTo(2);
                    assertThat(responseCountry.getStates()).hasSize(2);
                });

        verify(usersInteractionPort).findAllSortedByLocation();
    }

    @Test
    void getAllSortedByLocation_shouldReturnNoContent_whenNoUsersExist() {
        // Given
        when(usersInteractionPort.findAllSortedByLocation()).thenReturn(List.of());

        // When
        ResponseEntity<List<Country>> response = userController.getAllSortedByLocation();

        // Then
        assertThat(response).isEqualTo(ResponseEntity.noContent().build());
        assertThat(response.getBody()).isNull();
        verify(usersInteractionPort).findAllSortedByLocation();
    }
}
