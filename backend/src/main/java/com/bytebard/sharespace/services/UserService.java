package com.bytebard.sharespace.services;

import com.bytebard.sharespace.dtos.auth.UpdateUserDTO;
import com.bytebard.sharespace.dtos.auth.UserDTO;
import com.bytebard.sharespace.exceptions.AlreadyExistsException;
import com.bytebard.sharespace.exceptions.NotFoundException;
import com.bytebard.sharespace.models.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface UserService {

    User findByUsername(String username);

    User register(User user) throws AlreadyExistsException;

    UserDTO getUserById(Long id) throws NotFoundException;

    List<UserDTO> getAllUsers(String searchValue, int page, int perPage);

    User getCurrentUser();

    UserDTO getCurrentUserDTO() throws NotFoundException;

    UserDTO updateUser(Long userId, UpdateUserDTO userDTO, MultipartFile file) throws NotFoundException, IOException;
}
