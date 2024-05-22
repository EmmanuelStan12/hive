package com.bytebard.sharespace.services.impl;

import com.bytebard.sharespace.config.security.JwtAuthenticationToken;
import com.bytebard.sharespace.dtos.auth.UpdateUserDTO;
import com.bytebard.sharespace.dtos.auth.UserDTO;
import com.bytebard.sharespace.exceptions.AlreadyExistsException;
import com.bytebard.sharespace.exceptions.NotFoundException;
import com.bytebard.sharespace.files.FileUploadHelper;
import com.bytebard.sharespace.mappers.MapperUtils;
import com.bytebard.sharespace.models.Post;
import com.bytebard.sharespace.models.User;
import com.bytebard.sharespace.repository.UserRepository;
import com.bytebard.sharespace.services.UserService;
import javafx.util.Pair;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final FileUploadHelper fileUploadHelper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, FileUploadHelper fileUploadHelper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileUploadHelper = fileUploadHelper;
    }

    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailOrUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username does not exist."));
    }

    @Transactional
    public User register(User user) throws AlreadyExistsException {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new AlreadyExistsException("Email already exists");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new AlreadyExistsException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDTO getUserById(Long id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with Id + " + id + " does not exist."));
        return MapperUtils.toUserDTO(user, true, true);
    }

    @Override
    public List<UserDTO> getAllUsers(String searchValue, int page, int perPage) {
        List<User> users = userRepository.findAll();
        return users.stream().map(MapperUtils::toUserDTO).toList();
    }

    @Override
    public User getCurrentUser() {
        return currentUser();
    }

    @Override
    public UserDTO updateUser(Long userId, UpdateUserDTO userDTO, MultipartFile file) throws NotFoundException, IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        user.setName(userDTO.getName());
        user.setBio(userDTO.getBio());

        if (file != null) {
            fileUploadHelper.delete(user.getImageId());
            Pair<String, String> fileProps = fileUploadHelper.upload(file);

            user.setImageId(fileProps.getKey());
            user.setImageUrl(fileProps.getValue());

            user = userRepository.save(user);
        }
        return MapperUtils.toUserDTO(user);
    }

    @Override
    public UserDTO getCurrentUserDTO() throws NotFoundException {
        User user = userRepository.findById(currentUser().getId()).orElseThrow(() -> new NotFoundException("User not found"));

        return MapperUtils.toUserDTO(user, true, true);
    }

    private User currentUser() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
