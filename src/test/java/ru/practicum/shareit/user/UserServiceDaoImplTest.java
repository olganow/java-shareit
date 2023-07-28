package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceDaoImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceDaoImpl userService;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("User_name")
                .email("user@test.testz")
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .name("User_name")
                .email("user@test.testz")
                .build();

    }

    @Test
    public void createUserTest() {
        when(userRepository.save(any())).thenReturn(user);

        UserDto actualNewUser = userService.createUser(userDto);
        assertNotNull(actualNewUser);
        assertEquals(user.getId(), actualNewUser.getId());
        assertEquals(user.getEmail(), actualNewUser.getEmail());
        verify(userRepository, times(1)).save(any());
    }


    @Test
    public void getUserByIdTest() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        UserDto actualUser = userService.getUserById(1L);
        assertNotNull(actualUser);
        assertEquals(user.getId(), actualUser.getId());
        assertEquals(user.getEmail(), actualUser.getEmail());
    }

    @Test
    public void getAllUsersTest() {
        List<UserDto> expectedUserList = new ArrayList<>();
        expectedUserList.add(userDto);
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<UserDto> actualUserList = userService.getAllUsers();

        assertFalse(actualUserList.isEmpty());
        assertEquals(1, actualUserList.size());
        assertEquals(expectedUserList, actualUserList);
    }
}