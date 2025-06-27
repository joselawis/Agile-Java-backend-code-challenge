package com.jlcm.code.challenge.msvc.users.infrastructure.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jlcm.code.challenge.msvc.users.domain.dto.Country;
import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.domain.ports.input.UsersInteractionPort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Tag(name = "User Management", description = "Operations for managing a collection of users")
@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UsersInteractionPort usersInteractionPort;

    public UserController(UsersInteractionPort usersInteractionPort) {
        this.usersInteractionPort = usersInteractionPort;
    }

    @Operation(summary = "Get All Users", description = "Return the list of all users")
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

    @Operation(summary = "Get All Users paginated", description = "Return a paginated list of all users")
    @GetMapping("/paginated")
    public ResponseEntity<Page<User>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        logger.info("Fetching all users with pagination: page={}, size={}, sortBy={}, sortDir={}", page, size, sortBy,
                sortDir);
        Page<User> pagedResponse = usersInteractionPort.findAllPaginated(page, size, sortBy, sortDir);
        if (pagedResponse.getContent().isEmpty()) {
            logger.warn("No users found for the given pagination parameters");
            return ResponseEntity.noContent().build();
        }
        logger.info("Found {} users paginated", pagedResponse.getContent().size());
        return ResponseEntity.ok(pagedResponse);
    }

    @Operation(summary = "Get User by Username", description = "Return a single user")
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

    @Operation(summary = "Create User", description = "Create a user")
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

    @Operation(summary = "Update User", description = "Update the information of a single user")
    @PutMapping("/{username}")
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

    @Operation(summary = "Delete User", description = "Delete a single user")
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

    @Operation(summary = "Generate Users", description = "Generate a number, provided as a parameter, of random users. To create the users you have to use the https://randomuser.me [Random User Generator] service. Users will be added to the collection of existing users")
    @GetMapping("/generate/{count}")
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

    @Operation(summary = "Get Users Sorted by Location", description = "Return a tree with the users grouped by country, state and city (It can't be done in database)")
    @GetMapping("/tree")
    public ResponseEntity<List<Country>> getAllSortedByLocation() {
        logger.info("Finding users sorted by country, state and city");
        List<Country> usersSorted = (List<Country>) usersInteractionPort.findAllSortedByLocation();
        if (usersSorted.isEmpty()) {
            logger.warn("No users found");
            return ResponseEntity.noContent().build();
        }
        logger.info("Found {} users", usersSorted.size());
        return ResponseEntity.ok(usersSorted);
    }

}
