package com.example.later.user.model.dto;


import com.example.later.user.model.UserState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private UserState state;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthday;
}
