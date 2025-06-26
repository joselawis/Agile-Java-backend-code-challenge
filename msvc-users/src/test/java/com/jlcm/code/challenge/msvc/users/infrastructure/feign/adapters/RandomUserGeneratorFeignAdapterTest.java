package com.jlcm.code.challenge.msvc.users.infrastructure.feign.adapters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jlcm.code.challenge.msvc.users.domain.entities.FullName;
import com.jlcm.code.challenge.msvc.users.domain.entities.Gender;
import com.jlcm.code.challenge.msvc.users.domain.entities.Location;
import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.client.RandomUserGeneratorFeignClient;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.LocationDto;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.LoginDto;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.NameDto;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.PictureDto;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.RandomUserResponseDto;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.UserResultDto;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.mappers.RandomUserMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("RandomUserGeneratorFeignAdapter Tests")
class RandomUserGeneratorFeignAdapterTest {

        @Mock
        private RandomUserGeneratorFeignClient randomUserGeneratorFeignClient;

        @Mock
        private RandomUserMapper randomUserMapper;

        @InjectMocks
        private RandomUserGeneratorFeignAdapter randomUserGeneratorFeignAdapter;

        private UserResultDto userResultDto1;
        private UserResultDto userResultDto2;
        private User domainUser1;
        private User domainUser2;
        private RandomUserResponseDto responseDto;

        @BeforeEach
        void setUp() {
                // Setup DTOs
                userResultDto1 = UserResultDto.builder()
                                .gender("male")
                                .name(new NameDto("Mr", "John", "Doe"))
                                .location(new LocationDto("Los Angeles", "California", "USA"))
                                .email("john.doe@example.com")
                                .login(new LoginDto("johndoe123"))
                                .picture(new PictureDto("https://randomuser.me/api/portraits/men/1.jpg"))
                                .build();

                userResultDto2 = UserResultDto.builder()
                                .gender("female")
                                .name(new NameDto("Ms", "Jane", "Smith"))
                                .location(new LocationDto("Toronto", "Ontario", "Canada"))
                                .email("jane.smith@example.com")
                                .login(new LoginDto("janesmith456"))
                                .picture(new PictureDto("https://randomuser.me/api/portraits/women/1.jpg"))
                                .build();

                // Setup Domain Users
                domainUser1 = User.builder()
                                .username("johndoe123")
                                .name(FullName.builder()
                                                .title("Mr")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .build())
                                .email("john.doe@example.com")
                                .gender(Gender.MALE)
                                .pictureUrl("https://randomuser.me/api/portraits/men/1.jpg")
                                .location(Location.builder()
                                                .country("USA")
                                                .state("California")
                                                .city("Los Angeles")
                                                .build())
                                .build();

                domainUser2 = User.builder()
                                .username("janesmith456")
                                .name(FullName.builder()
                                                .title("Ms")
                                                .firstName("Jane")
                                                .lastName("Smith")
                                                .build())
                                .email("jane.smith@example.com")
                                .gender(Gender.FEMALE)
                                .pictureUrl("https://randomuser.me/api/portraits/women/1.jpg")
                                .location(Location.builder()
                                                .country("Canada")
                                                .state("Ontario")
                                                .city("Toronto")
                                                .build())
                                .build();

                responseDto = new RandomUserResponseDto();
                responseDto.setResults(Arrays.asList(userResultDto1, userResultDto2));
        }

        @Test
        @DisplayName("Should generate users successfully when API returns data")
        void shouldGenerateUsersSuccessfullyWhenApiReturnsData() {
                // Given
                int count = 2;
                String expectedFields = "gender,name,location,email,login,picture";

                when(randomUserGeneratorFeignClient.getRandomUsers(count, expectedFields))
                                .thenReturn(responseDto);
                when(randomUserMapper.toDomain(userResultDto1)).thenReturn(domainUser1);
                when(randomUserMapper.toDomain(userResultDto2)).thenReturn(domainUser2);

                // When
                Collection<User> result = randomUserGeneratorFeignAdapter.generateUsers(count);

                // Then
                assertThat(result)
                                .isNotNull()
                                .hasSize(2)
                                .containsExactly(domainUser1, domainUser2);

                verify(randomUserGeneratorFeignClient).getRandomUsers(count, expectedFields);
                verify(randomUserMapper).toDomain(userResultDto1);
                verify(randomUserMapper).toDomain(userResultDto2);
        }

        @Test
        @DisplayName("Should return empty collection when API returns empty results")
        void shouldReturnEmptyCollectionWhenApiReturnsEmptyResults() {
                // Given
                int count = 0;
                String expectedFields = "gender,name,location,email,login,picture";
                RandomUserResponseDto emptyResponse = new RandomUserResponseDto();
                emptyResponse.setResults(List.of());

                when(randomUserGeneratorFeignClient.getRandomUsers(count, expectedFields))
                                .thenReturn(emptyResponse);

                // When
                Collection<User> result = randomUserGeneratorFeignAdapter.generateUsers(count);

                // Then
                assertThat(result)
                                .isNotNull()
                                .isEmpty();

                verify(randomUserGeneratorFeignClient).getRandomUsers(count, expectedFields);
        }

        @ParameterizedTest
        @ValueSource(ints = { 1, 5, 10, 25, 50 })
        @DisplayName("Should request correct count and fields for different user counts")
        void shouldRequestCorrectCountAndFieldsForDifferentUserCounts(int count) {
                // Given
                String expectedFields = "gender,name,location,email,login,picture";
                RandomUserResponseDto emptyResponse = new RandomUserResponseDto();
                emptyResponse.setResults(List.of());

                when(randomUserGeneratorFeignClient.getRandomUsers(count, expectedFields))
                                .thenReturn(emptyResponse);

                // When
                randomUserGeneratorFeignAdapter.generateUsers(count);

                // Then
                verify(randomUserGeneratorFeignClient).getRandomUsers(count, expectedFields);
        }

        @Test
        @DisplayName("Should handle single user generation correctly")
        void shouldHandleSingleUserGenerationCorrectly() {
                // Given
                int count = 1;
                String expectedFields = "gender,name,location,email,login,picture";
                RandomUserResponseDto singleUserResponse = new RandomUserResponseDto();
                singleUserResponse.setResults(List.of(userResultDto1));

                when(randomUserGeneratorFeignClient.getRandomUsers(count, expectedFields))
                                .thenReturn(singleUserResponse);
                when(randomUserMapper.toDomain(userResultDto1)).thenReturn(domainUser1);

                // When
                Collection<User> result = randomUserGeneratorFeignAdapter.generateUsers(count);

                // Then
                assertThat(result)
                                .isNotNull()
                                .hasSize(1)
                                .containsExactly(domainUser1);

                verify(randomUserGeneratorFeignClient).getRandomUsers(count, expectedFields);
                verify(randomUserMapper).toDomain(userResultDto1);
        }

        @Test
        @DisplayName("Should always request the same fields regardless of count")
        void shouldAlwaysRequestTheSameFieldsRegardlessOfCount() {
                // Given
                int count = 3;
                String expectedFields = "gender,name,location,email,login,picture";
                RandomUserResponseDto response = new RandomUserResponseDto();
                response.setResults(List.of());

                when(randomUserGeneratorFeignClient.getRandomUsers(anyInt(), anyString()))
                                .thenReturn(response);

                // When
                randomUserGeneratorFeignAdapter.generateUsers(count);

                // Then
                verify(randomUserGeneratorFeignClient).getRandomUsers(count, expectedFields);
        }

        @Test
        @DisplayName("Should map all returned DTOs to domain objects")
        void shouldMapAllReturnedDtosTodomainObjects() {
                // Given
                int count = 2;
                String expectedFields = "gender,name,location,email,login,picture";

                when(randomUserGeneratorFeignClient.getRandomUsers(count, expectedFields))
                                .thenReturn(responseDto);
                when(randomUserMapper.toDomain(any(UserResultDto.class)))
                                .thenReturn(domainUser1, domainUser2);

                // When
                Collection<User> result = randomUserGeneratorFeignAdapter.generateUsers(count);

                // Then
                assertThat(result)
                                .isNotNull()
                                .hasSize(2);

                verify(randomUserGeneratorFeignClient).getRandomUsers(count, expectedFields);
                verify(randomUserMapper).toDomain(userResultDto1);
                verify(randomUserMapper).toDomain(userResultDto2);
        }
}
