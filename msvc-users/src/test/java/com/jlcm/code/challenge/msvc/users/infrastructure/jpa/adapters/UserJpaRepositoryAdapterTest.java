package com.jlcm.code.challenge.msvc.users.infrastructure.jpa.adapters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.jlcm.code.challenge.msvc.users.domain.entities.FullName;
import com.jlcm.code.challenge.msvc.users.domain.entities.Gender;
import com.jlcm.code.challenge.msvc.users.domain.entities.Location;
import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.infrastructure.jpa.entities.UserJpaEntity;
import com.jlcm.code.challenge.msvc.users.infrastructure.jpa.mappers.UserJpaMapper;
import com.jlcm.code.challenge.msvc.users.infrastructure.jpa.repositories.UserJpaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserJpaRepositoryAdapter Tests")
class UserJpaRepositoryAdapterTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private UserJpaMapper userJpaMapper;

    @InjectMocks
    private UserJpaRepositoryAdapter userJpaRepositoryAdapter;

    private User domainUser;
    private UserJpaEntity jpaEntity;
    private UserJpaEntity anotherJpaEntity;
    private User anotherDomainUser;

    @BeforeEach
    void setUp() {
        domainUser = User.builder()
                .username("johndoe")
                .name(FullName.builder()
                        .title("Mr")
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .email("john.doe@example.com")
                .gender(Gender.MALE)
                .pictureUrl("https://example.com/picture.jpg")
                .location(Location.builder()
                        .country("USA")
                        .state("California")
                        .city("Los Angeles")
                        .build())
                .build();

        jpaEntity = UserJpaEntity.builder()
                .id(1L)
                .username("johndoe")
                .title("Mr")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .gender(1)
                .pictureUrl("https://example.com/picture.jpg")
                .country("USA")
                .state("California")
                .city("Los Angeles")
                .build();

        anotherJpaEntity = UserJpaEntity.builder()
                .id(2L)
                .username("janedoe")
                .title("Ms")
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .gender(2)
                .pictureUrl("https://example.com/jane.jpg")
                .country("Canada")
                .state("Ontario")
                .city("Toronto")
                .build();

        anotherDomainUser = User.builder()
                .username("janedoe")
                .name(FullName.builder()
                        .title("Ms")
                        .firstName("Jane")
                        .lastName("Doe")
                        .build())
                .email("jane.doe@example.com")
                .gender(Gender.FEMALE)
                .pictureUrl("https://example.com/jane.jpg")
                .location(Location.builder()
                        .country("Canada")
                        .state("Ontario")
                        .city("Toronto")
                        .build())
                .build();
    }

    @Nested
    @DisplayName("findAll() Tests")
    class FindAllTests {

        @Test
        @DisplayName("Should return all users when repository has data")
        void shouldReturnAllUsersWhenRepositoryHasData() {
            // Given
            List<UserJpaEntity> jpaEntities = Arrays.asList(jpaEntity, anotherJpaEntity);
            when(userJpaRepository.findAll()).thenReturn(jpaEntities);
            when(userJpaMapper.toDomain(jpaEntity)).thenReturn(domainUser);
            when(userJpaMapper.toDomain(anotherJpaEntity)).thenReturn(anotherDomainUser);

            // When
            Collection<User> result = userJpaRepositoryAdapter.findAll();

            // Then
            assertThat(result)
                    .isNotNull()
                    .hasSize(2)
                    .containsExactly(domainUser, anotherDomainUser);

            verify(userJpaRepository).findAll();
            verify(userJpaMapper).toDomain(jpaEntity);
            verify(userJpaMapper).toDomain(anotherJpaEntity);
        }

        @Test
        @DisplayName("Should return empty collection when repository is empty")
        void shouldReturnEmptyCollectionWhenRepositoryIsEmpty() {
            // Given
            when(userJpaRepository.findAll()).thenReturn(List.of());

            // When
            Collection<User> result = userJpaRepositoryAdapter.findAll();

            // Then
            assertThat(result)
                    .isNotNull()
                    .isEmpty();

            verify(userJpaRepository).findAll();
            verify(userJpaMapper, never()).toDomain(any(UserJpaEntity.class));
        }
    }

    @Nested
    @DisplayName("findByUsername() Tests")
    class FindByUsernameTests {

        @Test
        @DisplayName("Should return user when found by username")
        void shouldReturnUserWhenFoundByUsername() {
            // Given
            String username = "johndoe";
            when(userJpaRepository.findByUsername(username)).thenReturn(Optional.of(jpaEntity));
            when(userJpaMapper.toDomain(jpaEntity)).thenReturn(domainUser);

            // When
            Optional<User> result = userJpaRepositoryAdapter.findByUsername(username);

            // Then
            assertThat(result)
                    .isPresent()
                    .contains(domainUser);

            verify(userJpaRepository).findByUsername(username);
            verify(userJpaMapper).toDomain(jpaEntity);
        }

        @Test
        @DisplayName("Should return empty optional when user not found by username")
        void shouldReturnEmptyOptionalWhenUserNotFoundByUsername() {
            // Given
            String username = "nonexistent";
            when(userJpaRepository.findByUsername(username)).thenReturn(Optional.empty());

            // When
            Optional<User> result = userJpaRepositoryAdapter.findByUsername(username);

            // Then
            assertThat(result).isEmpty();

            verify(userJpaRepository).findByUsername(username);
            verify(userJpaMapper, never()).toDomain(any(UserJpaEntity.class));
        }
    }

    @Nested
    @DisplayName("create() Tests")
    class CreateTests {

        @Test
        @DisplayName("Should create and return user successfully")
        void shouldCreateAndReturnUserSuccessfully() {
            // Given
            when(userJpaMapper.toJpaEntity(domainUser)).thenReturn(jpaEntity);
            when(userJpaRepository.save(jpaEntity)).thenReturn(jpaEntity);
            when(userJpaMapper.toDomain(jpaEntity)).thenReturn(domainUser);

            // When
            Optional<User> result = userJpaRepositoryAdapter.create(domainUser);

            // Then
            assertThat(result)
                    .isPresent()
                    .contains(domainUser);

            verify(userJpaMapper).toJpaEntity(domainUser);
            verify(userJpaRepository).save(jpaEntity);
            verify(userJpaMapper).toDomain(jpaEntity);
        }
    }

    @Nested
    @DisplayName("update() Tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update and return user when user exists")
        void shouldUpdateAndReturnUserWhenUserExists() {
            // Given
            String username = "johndoe";
            UserJpaEntity updatedJpaEntity = UserJpaEntity.builder()
                    .id(1L)
                    .username("johndoe")
                    .title("Dr")
                    .firstName("John")
                    .lastName("Doe")
                    .email("john.doe@example.com")
                    .gender(1)
                    .pictureUrl("https://example.com/picture.jpg")
                    .country("USA")
                    .state("California")
                    .city("Los Angeles")
                    .build();

            User updatedDomainUser = User.builder()
                    .username("johndoe")
                    .name(FullName.builder()
                            .title("Dr")
                            .firstName("John")
                            .lastName("Doe")
                            .build())
                    .email("john.doe@example.com")
                    .gender(Gender.MALE)
                    .pictureUrl("https://example.com/picture.jpg")
                    .location(Location.builder()
                            .country("USA")
                            .state("California")
                            .city("Los Angeles")
                            .build())
                    .build();

            when(userJpaRepository.findByUsername(username)).thenReturn(Optional.of(jpaEntity));
            when(userJpaMapper.toJpaEntity(updatedDomainUser)).thenReturn(updatedJpaEntity);
            when(userJpaRepository.save(updatedJpaEntity)).thenReturn(updatedJpaEntity);
            when(userJpaMapper.toDomain(updatedJpaEntity)).thenReturn(updatedDomainUser);

            // When
            Optional<User> result = userJpaRepositoryAdapter.update(username, updatedDomainUser);

            // Then
            assertThat(result)
                    .isPresent()
                    .contains(updatedDomainUser);

            verify(userJpaRepository).findByUsername(username);
            verify(userJpaMapper).toJpaEntity(updatedDomainUser);
            verify(userJpaRepository).save(updatedJpaEntity);
            verify(userJpaMapper).toDomain(updatedJpaEntity);
        }

        @Test
        @DisplayName("Should return empty optional when user to update does not exist")
        void shouldReturnEmptyOptionalWhenUserToUpdateDoesNotExist() {
            // Given
            String username = "nonexistent";
            when(userJpaRepository.findByUsername(username)).thenReturn(Optional.empty());

            // When
            Optional<User> result = userJpaRepositoryAdapter.update(username, domainUser);

            // Then
            assertThat(result).isEmpty();

            verify(userJpaRepository).findByUsername(username);
            verify(userJpaMapper, never()).toJpaEntity(any(User.class));
            verify(userJpaRepository, never()).save(any(UserJpaEntity.class));
            verify(userJpaMapper, never()).toDomain(any(UserJpaEntity.class));
        }
    }

    @Nested
    @DisplayName("delete() Tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete and return user successfully")
        void shouldDeleteAndReturnUserSuccessfully() {
            // Given
            String username = "johndoe";
            when(userJpaRepository.findByUsername(username)).thenReturn(Optional.of(jpaEntity));
            when(userJpaMapper.toDomain(jpaEntity)).thenReturn(domainUser);

            // When
            Optional<User> result = userJpaRepositoryAdapter.delete(username);

            // Then
            assertThat(result)
                    .isPresent()
                    .contains(domainUser);

            verify(userJpaRepository).findByUsername(username);
            verify(userJpaRepository).delete(jpaEntity);
            verify(userJpaMapper).toDomain(jpaEntity);
        }

        @Test
        @DisplayName("Should return empty optional when user to delete does not exist")
        void shouldReturnEmptyOptionalWhenUserToDeleteDoesNotExist() {
            // Given
            String username = "nonexistent";
            when(userJpaRepository.findByUsername(username)).thenReturn(Optional.empty());

            // When
            Optional<User> result = userJpaRepositoryAdapter.delete(username);

            // Then
            assertThat(result).isEmpty();

            verify(userJpaRepository).findByUsername(username);
            verify(userJpaRepository, never()).delete(any(UserJpaEntity.class));
            verify(userJpaMapper, never()).toDomain(any(UserJpaEntity.class));
        }

        @Test
        @DisplayName("Should return empty optional when delete operation fails")
        void shouldReturnEmptyOptionalWhenDeleteOperationFails() {
            // Given
            String username = "johndoe";
            when(userJpaRepository.findByUsername(username)).thenReturn(Optional.of(jpaEntity));
            doThrow(new RuntimeException("Database error")).when(userJpaRepository).delete(jpaEntity);

            // When
            Optional<User> result = userJpaRepositoryAdapter.delete(username);

            // Then
            assertThat(result).isEmpty();

            verify(userJpaRepository).findByUsername(username);
            verify(userJpaRepository).delete(jpaEntity);
            verify(userJpaMapper, never()).toDomain(any(UserJpaEntity.class));
        }
    }

    @Nested
    @DisplayName("findAllPaginated() Tests")
    class FindAllPaginatedTests {

        @Test
        @DisplayName("Should return paginated users when repository has data")
        void shouldReturnPaginatedUsersWhenRepositoryHasData() {
            // Given
            int page = 0, size = 1;
            String sortBy = "username", sortDir = "asc";

            PageRequest expectedPageable = PageRequest.of(page, size,
                    Sort.by(Sort.Direction.ASC, sortBy));

            List<UserJpaEntity> content = List.of(jpaEntity);
            Page<UserJpaEntity> jpaPage = new PageImpl<>(content, expectedPageable, 1);

            when(userJpaRepository.findAll(expectedPageable)).thenReturn(jpaPage);
            when(userJpaMapper.toDomain(jpaEntity)).thenReturn(domainUser);

            // When
            Page<User> result = userJpaRepositoryAdapter.findAllPaginated(page, size, sortBy, sortDir);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1).containsExactly(domainUser);
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getNumber()).isZero();

            verify(userJpaRepository).findAll(expectedPageable);
            verify(userJpaMapper).toDomain(jpaEntity);
        }
    }
}
