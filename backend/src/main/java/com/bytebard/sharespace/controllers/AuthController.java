package com.bytebard.sharespace.controllers;

import com.bytebard.sharespace.config.security.JwtAuthenticationToken;
import com.bytebard.sharespace.config.security.JwtAuthenticationTokenProvider;
import com.bytebard.sharespace.dtos.ApiResponse;
import com.bytebard.sharespace.dtos.auth.AuthDTO;
import com.bytebard.sharespace.dtos.auth.CreateUserDTO;
import com.bytebard.sharespace.dtos.auth.LoginDTO;
import com.bytebard.sharespace.dtos.auth.UserDTO;
import com.bytebard.sharespace.models.User;
import com.bytebard.sharespace.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final UserService userService;

    private final JwtAuthenticationTokenProvider authenticationProvider;

    private final UserMapper userMapper;

    public AuthController(final UserService userService, JwtAuthenticationTokenProvider authenticationProvider, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationProvider = authenticationProvider;
        this.userMapper = userMapper;
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<AuthDTO>> login(@RequestBody LoginDTO loginDTO) {
        User user = authenticationProvider.authenticate(loginDTO.getEmail(), loginDTO.getPassword());
        AuthDTO response = generateAuthResponse(user);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Login successful", response), HttpStatus.OK);
    }

    @PostMapping("signup")
    public ResponseEntity<ApiResponse<AuthDTO>> signup(@Valid  @RequestBody CreateUserDTO createUserDTO) throws Exception {
        User user = userMapper.convertToModel(createUserDTO);
        user = userService.register(user);
        authenticationProvider.verifyUser(user);
        AuthDTO response = generateAuthResponse(user);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "Signup successful", response), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> me() throws Exception {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authenticationToken.getPrincipal();
        UserDTO userDTO = userMapper.convertToDTO(user);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, userDTO), HttpStatus.OK);
    }

    private AuthDTO generateAuthResponse(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new AuthDTO(userMapper.convertToDTO(user), authentication.getCredentials().toString());
    }
}
