package com.example.later.user.service;

import com.example.later.user.model.dto.UserCreationDto;
import com.example.later.user.model.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto saveUser(UserCreationDto userCreationDto);
    UserDto getUserById(long userId);
}
