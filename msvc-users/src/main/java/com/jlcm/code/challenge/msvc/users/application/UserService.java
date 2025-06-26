package com.jlcm.code.challenge.msvc.users.application;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlcm.code.challenge.msvc.users.domain.User;
import com.jlcm.code.challenge.msvc.users.domain.dto.City;
import com.jlcm.code.challenge.msvc.users.domain.dto.Country;
import com.jlcm.code.challenge.msvc.users.domain.dto.State;
import com.jlcm.code.challenge.msvc.users.domain.repository.UsersRepositoryPort;

@Service
public class UserService implements UsersInteractionPort {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UsersRepositoryPort usersRepository;

    public UserService(UsersRepositoryPort usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<User> findAll() {
        return usersRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUsername(String username) {
        return usersRepository.findByUsername(username);
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
        Optional<User> existingUser = usersRepository.findByUsername(username);
        if (existingUser.isEmpty()) {
            logger.warn("User {} not found for update", username);
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
        Optional<User> existingUser = usersRepository.findByUsername(username);
        if (existingUser.isEmpty()) {
            logger.warn("User {} not found for deletion", username);
            return Optional.empty();
        }
        return usersRepository.delete(username);
    }

    @Transactional
    @Override
    public Collection<User> generateUsers(int count) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateUsers'");
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

                    // Second level grouping by state
                    Map<String, List<User>> usersByState = countryEntry.getValue().stream()
                            .collect(Collectors.groupingBy(user -> user.getLocation().getState()));

                    List<State> states = usersByState.entrySet().stream()
                            .map(stateEntry -> {
                                String stateName = stateEntry.getKey();

                                // Third level grouping by city
                                Map<String, List<User>> usersByCity = stateEntry.getValue().stream()
                                        .collect(Collectors.groupingBy(user -> user.getLocation().getCity()));

                                List<City> cities = usersByCity.entrySet().stream()
                                        .map(cityEntry -> {
                                            String cityName = cityEntry.getKey();
                                            List<User> usersInCity = cityEntry.getValue();
                                            return new City(cityName, usersInCity);
                                        }).toList();
                                return new State(stateName, cities);
                            }).toList();

                    return new Country(countryName, states);
                })
                .toList();
    }

}
