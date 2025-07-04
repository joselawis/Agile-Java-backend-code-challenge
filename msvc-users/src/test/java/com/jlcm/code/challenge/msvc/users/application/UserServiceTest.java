package com.jlcm.code.challenge.msvc.users.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jlcm.code.challenge.msvc.users.domain.UserNotFoundException;
import com.jlcm.code.challenge.msvc.users.domain.dto.Country;
import com.jlcm.code.challenge.msvc.users.domain.entities.FullName;
import com.jlcm.code.challenge.msvc.users.domain.entities.Gender;
import com.jlcm.code.challenge.msvc.users.domain.entities.Location;
import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.domain.ports.output.UserGenerationPort;
import com.jlcm.code.challenge.msvc.users.domain.ports.output.UsersRepositoryPort;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UsersRepositoryPort usersRepository;

    @Mock
    private UserGenerationPort userGenerationPort;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("testuser")
                .name(FullName.builder()
                        .firstName("Test")
                        .lastName("User")
                        .build())
                .email("test@example.com")
                .gender(Gender.MALE)
                .location(Location.builder()
                        .country("España")
                        .state("Madrid")
                        .city("Madrid")
                        .build())
                .pictureUrl("https://example.com/pic.jpg")
                .build();
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        // Given
        List<User> expectedUsers = List.of(testUser);
        when(usersRepository.findAll()).thenReturn(expectedUsers);

        // When
        Collection<User> result = userService.findAll();

        // Then
        assertThat(result)
                .hasSize(1)
                .containsExactly(testUser);
        verify(usersRepository).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoUsers() {
        // Given
        when(usersRepository.findAll()).thenReturn(List.of());

        // When
        Collection<User> result = userService.findAll();

        // Then
        assertThat(result).isEmpty();
        verify(usersRepository).findAll();
    }

    @Test
    void findByUsername_shouldReturnUserWhenExists() {
        // Given
        String username = "testuser";
        when(usersRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.findByUsername(username);

        // Then
        assertThat(result).contains(testUser);
        verify(usersRepository).findByUsername(username);
    }

    @Test
    void findByUsername_shouldReturnEmptyWhenUserNotExists() {
        // Given
        String username = "nonexistent";
        when(usersRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> {
            userService.findByUsername2(username);
        }).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void create_shouldCreateUserSuccessfully() {
        // Given
        when(usersRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());
        when(usersRepository.create(testUser)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.create(testUser);

        // Then
        assertThat(result).contains(testUser);
        verify(usersRepository).findByUsername(testUser.getUsername());
        verify(usersRepository).create(testUser);
    }

    @ParameterizedTest(name = "create should return empty when user data is invalid: {0}")
    @MethodSource("invalidUserDataForCreate")
    void create_shouldReturnEmptyWhenUserDataIsInvalid(String testCase, User invalidUser) {
        // When
        Optional<User> result = userService.create(invalidUser);

        // Then
        assertThat(result).isEmpty();
    }

    private static Stream<Arguments> invalidUserDataForCreate() {
        return Stream.of(
                Arguments.of("user is null", null),
                Arguments.of("username is null", User.builder().username(null).email("test@example.com").build()),
                Arguments.of("username is blank", User.builder().username("").email("test@example.com").build()));
    }

    @Test
    void create_shouldReturnEmptyWhenUserAlreadyExists() {
        // Given
        when(usersRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.create(testUser);

        // Then
        assertThat(result).isEmpty();
        verify(usersRepository).findByUsername(testUser.getUsername());
    }

    @Test
    void update_shouldUpdateUserSuccessfully() {
        // Given
        String username = "testuser";
        when(usersRepository.update(username, testUser)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.update(username, testUser);

        // Then
        assertThat(result).contains(testUser);
        verify(usersRepository).update(username, testUser);
    }

    @ParameterizedTest(name = "update should return empty when parameters are invalid: {0}")
    @MethodSource("invalidUpdateParameters")
    void update_shouldReturnEmptyWhenParametersAreInvalid(String testCase, String username, User user) {
        // When
        Optional<User> result = userService.update(username, user);

        // Then
        assertThat(result).isEmpty();
    }

    private static Stream<Arguments> invalidUpdateParameters() {
        User validUser = User.builder().username("testuser").email("test@example.com").build();
        return Stream.of(
                Arguments.of("user is null", "testuser", null),
                Arguments.of("username is null", null, validUser),
                Arguments.of("username is blank", "", validUser),
                Arguments.of("usernames mismatch", "differentuser", validUser));
    }

    @Test
    void delete_shouldDeleteUserSuccessfully() {
        // Given
        String username = "testuser";
        when(usersRepository.delete(username)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.delete(username);

        // Then
        assertThat(result).contains(testUser);

        verify(usersRepository).delete(username);
    }

    @ParameterizedTest(name = "delete should return empty when username is invalid: {0}")
    @ValueSource(strings = { "", " " })
    void delete_shouldReturnEmptyWhenUsernameIsInvalid(String invalidUsername) {
        // When
        Optional<User> result = userService.delete(invalidUsername);

        // Then
        assertThat(result).isEmpty();

        verify(usersRepository, never()).findByUsername(anyString());
    }

    @Test
    void delete_shouldReturnEmptyWhenUsernameIsNull() {
        // When
        Optional<User> result = userService.delete(null);

        // Then
        assertThat(result).isEmpty();

        verify(usersRepository, never()).findByUsername(anyString());
    }

    @Test
    void generateUsers_shouldGenerateUsersSuccessfully() {
        // Given
        int count = 3;
        List<User> generatedUsers = List.of(testUser);
        when(userGenerationPort.generateUsers(count)).thenReturn(generatedUsers);
        when(usersRepository.create(any(User.class))).thenReturn(Optional.of(testUser));

        // When
        Collection<User> result = userService.generateUsers(count);

        // Then
        assertThat(result)
                .hasSize(1)
                .containsExactly(testUser);
        verify(userGenerationPort).generateUsers(count);
        verify(usersRepository).create(testUser);
    }

    @ParameterizedTest(name = "generateUsers should return empty when count is invalid: {0}")
    @ValueSource(ints = { 0, -1, -5 })
    void generateUsers_shouldReturnEmptyListWhenCountIsInvalid(int invalidCount) {
        // When
        Collection<User> result = userService.generateUsers(invalidCount);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void generateUsers_shouldReturnEmptyListWhenNoUsersGenerated() {
        // Given
        int count = 5;
        when(userGenerationPort.generateUsers(count)).thenReturn(List.of());

        // When
        Collection<User> result = userService.generateUsers(count);

        // Then
        assertThat(result).isEmpty();
        verify(userGenerationPort).generateUsers(count);
    }

    @Test
    void generateUsers_shouldFilterUsersWithNullUsername() {
        // Given
        int count = 2;
        User userWithNullUsername = User.builder()
                .username(null)
                .email("null@example.com")
                .build();
        List<User> generatedUsers = List.of(testUser, userWithNullUsername);
        when(userGenerationPort.generateUsers(count)).thenReturn(generatedUsers);
        when(usersRepository.create(testUser)).thenReturn(Optional.of(testUser));

        // When
        Collection<User> result = userService.generateUsers(count);

        // Then
        assertThat(result)
                .hasSize(1)
                .containsExactly(testUser);
        verify(userGenerationPort).generateUsers(count);
        verify(usersRepository).create(testUser);
    }

    @Test
    void findAllSortedByLocation_shouldReturnEmptyListWhenNoUsers() {
        // Given
        when(usersRepository.findAll()).thenReturn(List.of());

        // When
        List<Country> result = userService.findAllSortedByLocation();

        // Then
        assertThat(result).isEmpty();
        verify(usersRepository).findAll();
    }

    @Test
    void findAllSortedByLocation_shouldGroupUsersByLocation() {
        // Given
        User user1 = User.builder()
                .username("user1")
                .location(Location.builder()
                        .country("España")
                        .state("Madrid")
                        .city("Madrid")
                        .build())
                .build();

        User user2 = User.builder()
                .username("user2")
                .location(Location.builder()
                        .country("España")
                        .state("Catalunya")
                        .city("Barcelona")
                        .build())
                .build();

        List<User> users = List.of(user1, user2);
        when(usersRepository.findAll()).thenReturn(users);

        // When
        List<Country> result = userService.findAllSortedByLocation();

        // Then
        assertThat(result).hasSize(1);

        Country spain = result.get(0);
        assertThat(spain.getName()).isEqualTo("España");
        assertThat(spain.getNumberOfUsers()).isEqualTo(2);
        assertThat(spain.getStates()).hasSize(2);

        // Verificar estados
        assertThat(spain.getStates())
                .anyMatch(state -> state.getName().equals("Madrid") && state.getNumberOfUsers() == 1);
        assertThat(spain.getStates())
                .anyMatch(state -> state.getName().equals("Catalunya") && state.getNumberOfUsers() == 1);

        verify(usersRepository).findAll();
    }
}
