package com.myproject.services;

import com.myproject.dtos.UserRegistrationDto;
import com.myproject.models.User;
import com.myproject.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleService roleService;
    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService
    ){
        this.userRepository = userRepository;
        this.roleService = roleService;
    }
    @Override
    public User save(UserRegistrationDto userRegistrationDto) {
        User user = new User(
                userRegistrationDto.getName(),
                userRegistrationDto.getMobile(),
                userRegistrationDto.getEmail(),
                userRegistrationDto.getPassword(),
                userRegistrationDto.getRoles().stream().map(roleService::getRoleByName).toList()
        );
        return userRepository.save(user);
    }
}
