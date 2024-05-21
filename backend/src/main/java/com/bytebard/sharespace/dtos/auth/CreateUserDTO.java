package com.bytebard.sharespace.dtos.auth;

import com.bytebard.sharespace.validators.ValidEmail;
import com.bytebard.sharespace.validators.ValidPassword;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateUserDTO {

    @NotEmpty(message = "Firstname cannot be empty")
    private String name;

    @ValidEmail
    private String email;

    @ValidPassword
    private String password;

    @NotEmpty(message = "Username cannot be empty")
    private String username;
}
