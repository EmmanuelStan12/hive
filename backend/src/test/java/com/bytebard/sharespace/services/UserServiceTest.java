package com.bytebard.sharespace.services;

import com.bytebard.sharespace.exceptions.AlreadyExistsException;
import com.bytebard.sharespace.models.User;
import com.bytebard.sharespace.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test_findByUsername_existingUser() {
        createTestUser();
        String username = "test.username";
        User user = userService.findByUsername(username);
        assertNotNull(user);
        assertEquals(user.getUsername(), username);
    }

    @Test
    public void test_findByUsername_nonExistingUser() {
        String username = "test.username.1";
        assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername(username));
    }

    @Test
    public void test_findByEmail_existingUser() {
        createTestUser();
        String email = "test.email";
        User user = assertDoesNotThrow(() -> userService.findByUsername(email));
        assertEquals(user.getEmail(), email);
    }

    @Test
    public void test_findByEmail_nonExistingUser() {
        String email = "test.email.1";
        assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername(email));
    }

    @Test
    public void test_register_existingEmail() {
        createTestUser();
        User user = new User(
                null,
                "test.firstname",
                "test.lastname",
                "test.email",
                "test.password",
                "test.username"
        );
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> userService.register(user));
        assertEquals(exception.getMessage(), "Email already exists");
    }

    @Test
    public void test_register_existingUsername() {
        createTestUser();
        User user = new User(
                null,
                "test.firstname",
                "test.lastname",
                "test.email.1",
                "test.password",
                "test.username"
        );
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> userService.register(user));
        assertEquals(exception.getMessage(), "Username already exists");
    }

    @Test
    public void test_register_newUser() {
        User user = new User(
                null,
                "test.firstname",
                "test.lastname",
                "test.email1.1",
                "test.password",
                "test.username.1"
        );
        User createdUser = Assertions.assertDoesNotThrow(() -> userService.register(user));
        assertNotNull(createdUser);
        assertEquals(user.getUsername(), createdUser.getUsername());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertNotEquals("test.password", createdUser.getPassword());
    }

    private void createTestUser() {
        String password = passwordEncoder.encode("test.password");
        User user = new User(
                null,
                "test.firstname",
                "test.lastname",
                "test.email",
                password,
                "test.username"
        );

        userRepository.save(user);
    }
}
