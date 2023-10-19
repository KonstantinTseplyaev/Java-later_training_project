package com.example.later.user;

import com.example.later.user.model.dto.UserDto;
import com.example.later.user.model.UserState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class UserDtoJsonTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testUserDto() throws Exception {
        UserDto userDto = new UserDto(
                1L,
                "John",
                "Doe",
                UserState.ACTIVE,
                LocalDate.of(2000,10,5));

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.firstName").isEqualTo("John");
        assertThat(result).extractingJsonPathStringValue("$.lastName").isEqualTo("Doe");
        assertThat(result).extractingJsonPathStringValue("$.state").isEqualTo(UserState.ACTIVE.name());
        assertThat(result).extractingJsonPathStringValue("$.birthday").isEqualTo("05.10.2000");
    }
}
