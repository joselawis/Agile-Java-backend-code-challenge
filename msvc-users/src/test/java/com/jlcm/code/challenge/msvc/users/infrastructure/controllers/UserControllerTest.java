package com.jlcm.code.challenge.msvc.users.infrastructure.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.jlcm.code.challenge.msvc.users.application.UsersInteractionPort;
import com.jlcm.code.challenge.msvc.users.domain.User;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UsersInteractionPort usersInteractionPort;

    @InjectMocks
    private UserController userController;

    @Test
    public void getAll_shouldReturnNoContent_whenNoUsersExist() {
        when(usersInteractionPort.findAll()).thenReturn(List.of());

        ResponseEntity<List<User>> response = userController.getAll();

        assertEquals(ResponseEntity.noContent().build(), response);
        verify(usersInteractionPort).findAll();
        assertNull(response.getBody());
    }

    @SuppressWarnings("null")
    @Test
    public void getAll_shouldReturnUsers_whenUsersExist() {
        User user1 = User.builder().username("user1").build();
        User user2 = User.builder().username("user2").build();
        when(usersInteractionPort.findAll()).thenReturn(List.of(user1, user2));
        ResponseEntity<List<User>> response = userController.getAll();
        assertEquals(ResponseEntity.ok(List.of(user1, user2)), response);
        verify(usersInteractionPort).findAll();
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @SuppressWarnings("null")
    @Test
    public void getByUsername_shouldReturnUser_whenUserExists() {
        String username = "user1";
        User user = User.builder().username(username).build();
        when(usersInteractionPort.findByUsername(username)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getByUsername(username);

        assertEquals(ResponseEntity.ok(user), response);
        verify(usersInteractionPort).findByUsername(username);
        assertNotNull(response.getBody());
        assertEquals(username, response.getBody().getUsername());
    }

    @Test
    public void getByUsername_shouldReturnNotFound_whenUserDoesNotExist() {
        String username = "user1";
        when(usersInteractionPort.findByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getByUsername(username);

        assertEquals(ResponseEntity.notFound().build(), response);
        verify(usersInteractionPort).findByUsername(username);
        assertNull(response.getBody());
    }

    @SuppressWarnings("null")
    @Test
    public void create_shouldReturnCreatedUser() {
        String username = "newUser";
        User newUser = User.builder().username(username).build();
        when(usersInteractionPort.create(newUser)).thenReturn(Optional.of(newUser));

        ResponseEntity<User> response = userController.create(newUser);

        assertEquals(ResponseEntity.ok(newUser), response);
        verify(usersInteractionPort).create(newUser);
        assertNotNull(response.getBody());
        assertEquals(username, response.getBody().getUsername());
    }

    @Test
    public void create_shouldReturnBadRequest_whenUserIsNull() {
        ResponseEntity<User> response = userController.create(null);

        assertEquals(ResponseEntity.badRequest().build(), response);
        assertNull(response.getBody());
    }

    @Test
    public void create_shouldReturnBadRequest_whenUserDataIsInvalid() {
        User invalidUser = User.builder().username("").build(); // Assuming empty username is invalid
        ResponseEntity<User> response = userController.create(invalidUser);

        assertEquals(ResponseEntity.badRequest().build(), response);
        assertNull(response.getBody());
    }

    @Test
    public void create_shouldReturnBadRequest_whenUserExists() {
        String username = "existingUser";
        User newUser = User.builder().username(username).build();
        when(usersInteractionPort.create(newUser)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.create(newUser);

        assertEquals(ResponseEntity.badRequest().build(), response);
        verify(usersInteractionPort).create(newUser);
        assertNull(response.getBody());
    }

    @SuppressWarnings("null")
    @Test
    public void update_shouldReturnUpdatedUser_whenUserExists() {
        String username = "existingUser";

        User updatedUser = User.builder().username(username).build();
        when(usersInteractionPort.update(eq(username), any(User.class))).thenReturn(Optional.of(updatedUser));

        ResponseEntity<User> response = userController.update(username, updatedUser);

        assertEquals(ResponseEntity.ok(updatedUser), response);
        verify(usersInteractionPort).update(eq(username), any(User.class));
        assertNotNull(response.getBody());
        assertEquals(username, response.getBody().getUsername());
    }

    @Test
    public void update_shouldReturnNotFound_whenUserDoesNotExist() {
        String username = "nonExistingUser";
        User userData = User.builder().username(username).build();
        when(usersInteractionPort.update(eq(username), any(User.class))).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.update(username, userData);

        assertEquals(ResponseEntity.notFound().build(), response);
        verify(usersInteractionPort).update(eq(username), any(User.class));
        assertNull(response.getBody());
    }

    @Test
    public void update_shouldReturnBadRequest_whenUserDataIsInvalid() {
        String username = "invalidUser";
        User invalidUser = User.builder().username("").build();
        ResponseEntity<User> response = userController.update(username, invalidUser);

        assertEquals(ResponseEntity.badRequest().build(), response);
        assertNull(response.getBody());
    }

    @SuppressWarnings("null")
    @Test
    public void delete_shouldReturnDeletedUser_whenUserExists() {
        String username = "userToDelete";
        User deletedUser = User.builder().username(username).build();
        when(usersInteractionPort.delete(username)).thenReturn(Optional.of(deletedUser));

        ResponseEntity<User> response = userController.delete(username);

        assertEquals(ResponseEntity.ok(deletedUser), response);
        verify(usersInteractionPort).delete(username);
        assertNotNull(response.getBody());
        assertEquals(username, response.getBody().getUsername());
    }

    @Test
    public void delete_shouldReturnNotFound_whenUserDoesNotExist() {
        String username = "nonExistingUser";
        when(usersInteractionPort.delete(username)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.delete(username);

        assertEquals(ResponseEntity.notFound().build(), response);
        verify(usersInteractionPort).delete(username);
        assertNull(response.getBody());
    }

    @SuppressWarnings("null")
    @Test
    public void generate_shouldReturnGeneratedUsers() {
        int count = 5;
        List<User> generatedUsers = List.of(
                User.builder().username("user1").build(),
                User.builder().username("user2").build());
        when(usersInteractionPort.generateUsers(count)).thenReturn(generatedUsers);

        ResponseEntity<List<User>> response = userController.generate(count);

        assertEquals(ResponseEntity.ok(generatedUsers), response);
        verify(usersInteractionPort).generateUsers(count);
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void generate_shouldReturnNoContent_whenNoUsersGenerated() {
        int count = 0;
        when(usersInteractionPort.generateUsers(count)).thenReturn(List.of());

        ResponseEntity<List<User>> response = userController.generate(count);

        assertEquals(ResponseEntity.noContent().build(), response);
        verify(usersInteractionPort).generateUsers(count);
        assertNull(response.getBody());
    }

    @Test
    public void getAllSortedByLocation_shouldReturnUsersSortedByLocation() {
        // TODO: Implement this test
    }

    @Test
    public void getAllSortedByLocation_shouldReturnNoContent_whenNoUsersExist() {
        // TODO: Implement this test
    }
}
