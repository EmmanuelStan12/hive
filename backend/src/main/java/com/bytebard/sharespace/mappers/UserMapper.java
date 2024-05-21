package com.bytebard.sharespace.mappers;


import com.bytebard.sharespace.dtos.auth.CreateUserDTO;
import com.bytebard.sharespace.dtos.auth.UserDTO;
import com.bytebard.sharespace.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserDTO> {

    @Override
    public UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                user.getImageUrl(),
                user.getBio()
        );
    }

    @Override
    public User convertToModel(UserDTO userDTO) {
        return null;
    }

    public User convertToModel(final CreateUserDTO dto) {
        return new User(
                null,
                dto.getName(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getUsername(),
                null,
                null,
                null
        );
    }
}
