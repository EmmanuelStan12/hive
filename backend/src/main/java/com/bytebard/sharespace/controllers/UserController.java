package com.bytebard.sharespace.controllers;

import com.bytebard.sharespace.dtos.ApiResponse;
import com.bytebard.sharespace.dtos.auth.UpdateUserDTO;
import com.bytebard.sharespace.dtos.auth.UserDTO;
import com.bytebard.sharespace.exceptions.NotFoundException;
import com.bytebard.sharespace.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable("id") Long userId) throws NotFoundException {
        UserDTO userDTO = userService.getUserById(userId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, userDTO), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser() throws NotFoundException {
        UserDTO userDTO = userService.getCurrentUserDTO();
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, userDTO), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers(
            @RequestParam(value = "searchValue", defaultValue = "") String searchValue,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "perPage", defaultValue = "20") int perPage
    ) throws IOException {
        List<UserDTO> users = userService.getAllUsers(searchValue, page, perPage);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, users), HttpStatus.OK);
    }

    @PostMapping(value = "update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable("id") Long userId,
            @Valid @RequestPart("user") UpdateUserDTO createUserDTO,
            @RequestPart("file") MultipartFile file
    ) throws IOException, NotFoundException {
        UserDTO userDTO = userService.updateUser(userId, createUserDTO, file);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), null, userDTO), HttpStatus.OK);
    }
}
