package com.example.later.user;

import com.example.later.user.model.dto.UserCreationDto;
import com.example.later.user.model.dto.UserDto;
import com.example.later.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("GET-запрос: получение всех юзеров");
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDto saveNewUser(@RequestBody @Valid UserCreationDto userCreationDto) {
        log.info("POST-запрос: добавление нового юзера {}", userCreationDto);
        return userService.saveUser(userCreationDto);
    }
}
