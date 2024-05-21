package com.bytebard.sharespace.services;

import com.bytebard.sharespace.exceptions.AlreadyExistsException;
import com.bytebard.sharespace.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User findByUsername(String username);

    User register(User user) throws AlreadyExistsException;
}
