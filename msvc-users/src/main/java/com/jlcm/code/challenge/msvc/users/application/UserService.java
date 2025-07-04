package com.jlcm.code.challenge.msvc.users.application;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlcm.code.challenge.msvc.users.domain.UserNotFoundException;
import com.jlcm.code.challenge.msvc.users.domain.dto.City;
import com.jlcm.code.challenge.msvc.users.domain.dto.Country;
import com.jlcm.code.challenge.msvc.users.domain.dto.State;
import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.domain.ports.input.UsersInteractionPort;
import com.jlcm.code.challenge.msvc.users.domain.ports.output.UserGenerationPort;
import com.jlcm.code.challenge.msvc.users.domain.ports.output.UsersRepositoryPort;

@Service
public class UserService implements UsersInteractionPort {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UsersRepositoryPort usersRepository;
    private final UserGenerationPort userGenerationPort;

    public UserService(UsersRepositoryPort usersRepository, UserGenerationPort userGenerationPort) {
        this.usersRepository = usersRepository;
        this.userGenerationPort = userGenerationPort;
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<User> findAll() {
        return usersRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<User> findAllPaginated(int page, int size, String sortBy, String sortDir) {
        if (page < 0 || size <= 0) {
            logger.warn("Invalid pagination parameters: page={}, size={}", page, size);
            page = 0;
            size = 10;
        }
        if (sortBy == null || sortBy.isBlank()) {
            logger.warn("Invalid sortBy parameter: {}", sortBy);
            sortBy = "username";
        }
        if (sortDir == null || (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc"))) {
            logger.warn("Invalid sortDir parameter: {}", sortDir);
            sortDir = "asc";
        }

        return usersRepository.findAllPaginated(page, size, sortBy, sortDir);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public User findByUsername2(String username) {
        var user = usersRepository.findByUsername(username);
        return user.orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override
    public Optional<User> create(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().isBlank()) {
            logger.warn("Attempted to create a user with null or blank username");
            return Optional.empty();
        }
        if (usersRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.warn("User {} already exists", user.getUsername());
            return Optional.empty();
        }
        return usersRepository.create(user);
    }

    @Transactional
    @Override
    public Optional<User> update(String username, User user) {
        if (user == null || user.getUsername() == null || user.getUsername().isBlank()
                || username == null || username.isBlank()) {
            logger.warn("Attempted to update a user with null or blank username");
            return Optional.empty();
        }
        if (!username.equals(user.getUsername())) {
            logger.warn("Username in path {} does not match user object {}", username, user.getUsername());
            return Optional.empty();
        }

        return usersRepository.update(username, user);
    }

    @Transactional
    @Override
    public Optional<User> delete(String username) {
        if (username == null || username.isBlank()) {
            logger.warn("Attempted to delete a user with null or blank username");
            return Optional.empty();
        }
        return usersRepository.delete(username);
    }

    @Transactional
    @Override
    public Collection<User> generateUsers(int count) {
        if (count <= 0) {
            logger.warn("Attempted to generate users with non-positive count: {}", count);
            return List.of();
        }
        Collection<User> generatedUsers = userGenerationPort.generateUsers(count);
        if (generatedUsers.isEmpty()) {
            logger.info("No users generated");
            return List.of();
        }
        return generatedUsers.stream()
                .filter(user -> user.getUsername() != null && !user.getUsername().isBlank())
                .map(usersRepository::create)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Country> findAllSortedByLocation() {
        List<User> users = (List<User>) usersRepository.findAll();
        if (users.isEmpty()) {
            logger.info("No users found to sort by location");
            return List.of();
        }
        return users.stream()
                .collect(Collectors.groupingBy(user -> user.getLocation().getCountry()))
                .entrySet().stream()
                .map(countryEntry -> {
                    // First level grouping by country
                    String countryName = countryEntry.getKey();
                    int numberOfUsersInCountry = countryEntry.getValue().size();

                    // Second level grouping by state
                    Map<String, List<User>> usersByState = countryEntry.getValue().stream()
                            .collect(Collectors.groupingBy(user -> user.getLocation().getState()));

                    List<State> states = usersByState.entrySet().stream()
                            .map(stateEntry -> {
                                String stateName = stateEntry.getKey();
                                int numberOfUsersInState = stateEntry.getValue().size();

                                // Third level grouping by city
                                Map<String, List<User>> usersByCity = stateEntry.getValue().stream()
                                        .collect(Collectors.groupingBy(user -> user.getLocation().getCity()));

                                List<City> cities = usersByCity.entrySet().stream()
                                        .map(cityEntry -> {
                                            String cityName = cityEntry.getKey();
                                            List<User> usersInCity = cityEntry.getValue();
                                            return new City(cityName, usersInCity.size(), usersInCity);
                                        }).toList();
                                return new State(stateName, numberOfUsersInState, cities);
                            }).toList();

                    return new Country(countryName, numberOfUsersInCountry, states);
                })
                .toList();
    }

}
