package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(properties = {"db.name=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceDaoImplIntegrationTest {
    private final UserService userService;
    private UserDto userOne;
    private UserDto userSecond;

    @BeforeEach
    void beforeEach() {
        userOne = UserDto.builder()
                .id(1L)
                .name("User_name")
                .email("usersone@test.testz")
                .build();

        userSecond = UserDto.builder()
                .id(2L)
                .name("User_name")
                .email("usersecond@test.testz")
                .build();
    }

    @Test
    public void getAllUsersTest() {
        userService.createUser(userOne);
        userService.createUser(userSecond);
        List<UserDto> expectedUsers = List.of(userOne, userSecond);

        List<UserDto> actualUsers = userService.getAllUsers();
        assertEquals(expectedUsers.size(), actualUsers.size());
        assertEquals(expectedUsers.get(0).getName(), actualUsers.get(0).getName());
        assertEquals(expectedUsers.get(0).getEmail(), actualUsers.get(0).getEmail());
    }

}
