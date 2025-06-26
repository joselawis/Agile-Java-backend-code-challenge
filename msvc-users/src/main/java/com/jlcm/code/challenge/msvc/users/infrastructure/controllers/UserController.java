package com.jlcm.code.challenge.msvc.users.infrastructure.controllers;

import java.util.List;
import java.util.Optional;
import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlcm.code.challenge.msvc.users.application.UsersInteractionPort;
import com.jlcm.code.challenge.msvc.users.domain.User;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UsersInteractionPort usersInteractionPort;

    public UserController(UsersInteractionPort usersInteractionPort) {
        this.usersInteractionPort = usersInteractionPort;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        logger.info("Fetching all users");
        List<User> users = (List<User>) usersInteractionPort.findAll();
        if (users.isEmpty()) {
            logger.warn("No users found");
            return ResponseEntity.noContent().build();
        }
        logger.info("Found {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable String username) {
        logger.info("Fetching user: {}", username);
        return usersInteractionPort.findByUsername(username)
                .map(user -> {
                    logger.info("User {} found", user.getUsername());
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    logger.warn("User {} not found", username);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User newUser) {
        if (newUser == null || newUser.getUsername().isBlank()) {
            logger.error("Invalid user data provided for creation");
            return ResponseEntity.badRequest().build();
        }
        logger.info("Creating new user: {}", newUser.getUsername());
        return usersInteractionPort.create(newUser)
                .map(createdUser -> {
                    logger.info("User {} created successfully", createdUser.getUsername());
                    return ResponseEntity.ok(createdUser);
                })
                .orElseGet(() -> {
                    logger.error("Failed to create user: {}", newUser.getUsername());
                    return ResponseEntity.badRequest().build();
                });
    }

    @PutMapping("{username}")
    public ResponseEntity<User> update(@NotBlank @PathVariable String username, @Valid @RequestBody User userData) {
        if (username.isBlank() || userData.getUsername().isBlank()) {
            logger.error("Username cannot be blank");
            return ResponseEntity.badRequest().build();
        }
        logger.info("Updating user: {}", username);
        return usersInteractionPort.update(username, userData)
                .map(updatedUser -> {
                    logger.info("User {} updated successfully", updatedUser.getUsername());
                    return ResponseEntity.ok(updatedUser);
                })
                .orElseGet(() -> {
                    logger.warn("User {} not found for update", username);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<User> delete(@PathVariable String username) {
        logger.info("Deleting user: {}", username);
        return usersInteractionPort.delete(username)
                .map(deletedUser -> {
                    logger.info("User {} deleted successfully", deletedUser.getUsername());
                    return ResponseEntity.ok(deletedUser);
                })
                .orElseGet(() -> {
                    logger.warn("User {} not found for deletion", username);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("generate/{count}")
    public ResponseEntity<List<User>> generate(@PathVariable int count) {
        logger.info("Generating {} users", count);
        List<User> generatedUsers = (List<User>) usersInteractionPort.generateUsers(count);
        if (generatedUsers.isEmpty()) {
            logger.warn("No users generated");
            return ResponseEntity.noContent().build();
        }
        logger.info("Generated {} users", generatedUsers.size());
        return ResponseEntity.ok(generatedUsers);
    }

    // TODO: Think about a better way to handle this
    @GetMapping("tree")
    public ResponseEntity<List<User>> getAllSortedByLocation() {
        logger.info("Finding users sorted by country, state and city");
        SortedMap<String, List<User>> usersSorted = usersInteractionPort.findAllSortedByLocation();
        if (usersSorted.isEmpty()) {
            logger.warn("No users found");
            return ResponseEntity.noContent().build();
        }
        logger.info("Found {} users", usersSorted.size());
        return ResponseEntity.ok(usersSorted.values().stream().flatMap(List::stream).toList());
    }

}
