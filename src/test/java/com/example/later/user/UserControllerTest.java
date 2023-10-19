package com.example.later.user;

import com.example.later.user.model.dto.UserCreationDto;
import com.example.later.user.model.dto.UserDto;
import com.example.later.user.service.UserService;
import com.example.later.user.model.UserState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController controller;
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    private MockMvc mvc;
    private UserDto userDto;
    private UserDto user1Dto;
    private UserDto user2Dto;
    private UserCreationDto userCreationDto;

    @BeforeEach
    void setUp() throws ParseException {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        userDto = new UserDto(
                1L,
                "John",
                "Doe",
                UserState.ACTIVE,
                LocalDate.of(1993, 6, 5));

        user1Dto = new UserDto(
                2L,
                "faa",
                "fuu",
                UserState.ACTIVE,
                LocalDate.of(2004, 12, 19));

        user2Dto = new UserDto(
                3L,
                "kaaa",
                "kuu",
                UserState.ACTIVE,
                LocalDate.of(2000, 8, 14));

        userCreationDto = new UserCreationDto();
        userCreationDto.setEmail("john.doe@mail.com");
        userCreationDto.setFirstName("John");
        userCreationDto.setLastName("Doe");
        userCreationDto.setBirthday(LocalDate.of(2000, 11, 3));
    }

    @Test
    void saveNewUser() throws Exception {
        when(userService.saveUser(userCreationDto))
                .thenReturn(userDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.firstName", is(userDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(userDto.getLastName())))
                .andExpect(jsonPath("$.birthday", is(userDto.getBirthday().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))));
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(List.of(userDto, user1Dto, user2Dto));
        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }
}
