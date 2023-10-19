package com.example.later.user.service;

import com.example.later.mapper.MapperUtil;
import com.example.later.user.repository.UserRepository;
import com.example.later.user.model.User;
import com.example.later.user.model.dto.UserCreationDto;
import com.example.later.user.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        Page<User> users = userRepository.findAll(Pageable.ofSize(5));
        return MapperUtil.convertList(users.toList(), MapperUtil::convertToUserDto);
    }

    @Override
    @Transactional
    public UserDto saveUser(UserCreationDto userCreationDto) {
        User newUser = userRepository.save(MapperUtil.convertFromUserCreationDto(userCreationDto));
        return MapperUtil.convertToUserDto(newUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) throw new RuntimeException("юзер не найден");
        return MapperUtil.convertToUserDto(userOpt.get());
    }
}
