package com.myproject.services;

import com.myproject.dtos.UserRegistrationDto;
import com.myproject.models.User;

public interface UserService {
    User save(UserRegistrationDto userRegistrationDto);
}
