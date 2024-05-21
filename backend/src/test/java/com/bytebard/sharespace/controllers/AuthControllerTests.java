package com.bytebard.sharespace.controllers;

import com.bytebard.sharespace.exceptions.AlreadyExistsException;
import com.bytebard.sharespace.models.User;
import com.bytebard.sharespace.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;

    @Autowired
    private AuthController authController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void givenUsernameAndPassword_thenAuthenticate_andReturnUserDTOAndToken() throws Exception {
        createTestUser();

        mockMvc.perform(
                    post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\": \"test.username\", \"password\": \"test.password.1@Q\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.data.user.username", is("test.username")));
    }

    @Test
    public void givenUserDetails_thenSignupUser_andReturnUserDTOAndToken() throws Exception {
        createTestUser();
        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\": \"test.username\", \"password\": \"test.password.1@Q\", \"email\": \"test.email@email.com\", \"firstname\": \"test.firstname\", \"lastname\": \"test.lastname\" }")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.data.user.username", is("test.username")));
    }

    @Test
    public void givenUserDetails_thenSignupUser_andThrowValidationErrorForPassword() throws Exception {
        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\": \"test.username.1\", \"password\": \"test.password\", \"email\": \"test.email@email.com\", \"firstname\": \"test.firstname\", \"lastname\": \"test.lastname\" }")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenUserDetails_thenSignupUser_andThrowValidationErrorForEmail() throws Exception {
        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\": \"test.username.1\", \"password\": \"test.password.1@Q\", \"email\": \"test.email\", \"firstname\": \"test.firstname\", \"lastname\": \"test.lastname\" }")
                )
                .andExpect(status().isBadRequest());
    }

    private void createTestUser() throws AlreadyExistsException {
        String password = passwordEncoder.encode("test.password.1@Q");
        User user = new User(
                1L,
                "test.firstname",
                "test.lastname",
                "test.email",
                password,
                "test.username"
        );

        Mockito.when(userService.register(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(userService.findByUsername("test.username")).thenReturn(user);
    }
}
