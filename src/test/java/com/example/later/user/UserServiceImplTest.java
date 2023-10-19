package com.example.later.user;

import com.example.later.user.model.User;
import com.example.later.user.model.dto.UserCreationDto;
import com.example.later.user.service.UserService;
import com.example.later.user.model.UserState;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {
    private final EntityManager em;
    private final UserService service;

    @Test
    void saveUser() {
        UserCreationDto userCreationDto = makeUserCreationDto("some@email.com", "Пётр", "Иванов", LocalDate.of(2001, 12, 3));
        service.saveUser(userCreationDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query
                .setParameter("email", userCreationDto.getEmail())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getFirstName(), equalTo(userCreationDto.getFirstName()));
        assertThat(user.getLastName(), equalTo(userCreationDto.getLastName()));
        assertThat(user.getEmail(), equalTo(userCreationDto.getEmail()));
        assertThat(user.getBirthday(), equalTo(userCreationDto.getBirthday()));
        assertThat(user.getState(), equalTo(UserState.ACTIVE));
        assertThat(user.getRegistrationDate(), notNullValue());
    }

    @Test
    void getAllUsers() {
        UserCreationDto user1CreationDto = makeUserCreationDto("some2@email.com", "Максим", "Сидоров", LocalDate.of(1993, 5, 15));
        UserCreationDto user2CreationDto = makeUserCreationDto("some3@email.com", "Константин", "Бальмонт", LocalDate.of(2001, 6, 7));
        UserCreationDto user3CreationDto = makeUserCreationDto("some4@email.com", "Виктор", "Залупа", LocalDate.of(1980, 2, 23));
        service.saveUser(user1CreationDto);
        service.saveUser(user2CreationDto);
        service.saveUser(user3CreationDto);
        TypedQuery<User> query = em.createQuery("Select u from User u", User.class);
        List<User> users = query.getResultList();

        assertThat(users.get(0).getId(), notNullValue());
        assertThat(users.size(), equalTo(3));
    }

    private UserCreationDto makeUserCreationDto(String email, String firstName, String lastName, LocalDate birthday) {
        UserCreationDto userCreationDto = new UserCreationDto();
        userCreationDto.setEmail(email);
        userCreationDto.setFirstName(firstName);
        userCreationDto.setLastName(lastName);
        userCreationDto.setBirthday(birthday);
        return userCreationDto;
    }

}
