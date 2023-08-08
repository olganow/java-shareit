package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

    private User userAnother;
    private UserDto userAnotherDto;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("User_name")
                .email("user@test.testz")
                .build();

        userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        userAnother = User.builder()
                .id(2L)
                .name("UserAnother_name")
                .email("useranother@test.testz")
                .build();
        userAnotherDto = UserDto.builder()
                .id(userAnother.getId())
                .name(userAnother.getName())
                .email(userAnother.getEmail())
                .build();

    }

    @Test
    public void createUserTest() {
        when(userRepository.save(any())).thenReturn(user);
        UserDto actualNewUser = userService.createUser(userDto);

        assertNotNull(actualNewUser);
        assertEquals(user.getId(), actualNewUser.getId());
        assertEquals(user.getEmail(), actualNewUser.getEmail());
        assertThat(user).hasFieldOrProperty("id");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void createUserWithAlreadyExistsExceptionValidationTest() {
        when(userRepository.save(any())).thenThrow(AlreadyExistsException.class);

        assertThatThrownBy(() -> userService.createUser(userDto)).isInstanceOf(AlreadyExistsException.class);
    }

    @Test
    void createUserWithInvalidEmailValidationTest() {
        userDto.setEmail("invalidEmail");
        when(userRepository.save(any())).thenThrow(ValidationException.class);

        assertThatThrownBy(() -> userService.createUser(userDto)).isInstanceOf(ValidationException.class);
    }


    @Test
    public void getUserByIdTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        UserDto actualUser = userService.getUserById(1L);

        assertNotNull(actualUser);
        assertEquals(user.getId(), actualUser.getId());
        assertEquals(user.getEmail(), actualUser.getEmail());
    }

    @Test
    void getByIdWithNotFoundExceptionValidationTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(1L)).isInstanceOf(NotFoundException.class);
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

    @Test
    public void updateUserNameByIdTest() {
        Long userId = userAnother.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userAnother));
        user.setName("newname");
        UserDto actual = userService.updateUserById(userId, UserMapper.userToUserDto(userAnother));

        assertEquals(userAnother.getName(), actual.getName());
    }

    @Test
    public void updateUserEmailByIdTest() {
        Long userId = userAnother.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userAnother));
        user.setName("newemail@test.testz");
        UserDto actual = userService.updateUserById(userId, UserMapper.userToUserDto(userAnother));

        assertEquals(userAnother.getName(), actual.getName());
    }

    @Test
    void updateUserByIdWithWrongEmailValidationTest() {
        UserDto userAnotherDto = new UserDto(999L, "AnotherName", "useranother@test.testz");
        when(userRepository.findById(userAnotherDto.getId())).thenReturn(Optional.empty());
        Long userId = userAnotherDto.getId();

        assertThatThrownBy(() -> userService.updateUserById(userId, userAnotherDto)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateUserByIdWithExceptionTest() {
        UserDto userDtoThird = new UserDto(999L, "User_name", "user@test.testz");
        when(userRepository.findById(userDtoThird.getId())).thenReturn(Optional.empty());
        Long userId = 999L;

        assertThatThrownBy(() -> userService.updateUserById(userId, userDto)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateUserByIdWithAlreadyExistsExceptionValidationTest() {
        UserDto userDtoThird = new UserDto(999L, "User_name", "user@test.testz");
        when(userRepository.findById(userDtoThird.getId())).thenThrow(AlreadyExistsException.class);
        Long userId = 999L;
        assertThatThrownBy(() -> userService.updateUserById(userId, userDto)).isInstanceOf(AlreadyExistsException.class);
    }

    @Test
    public void removeUserByIdTest() {
        doNothing().when(userRepository).deleteById(anyLong());
        userService.removeUserById(userAnotherDto.getId());
        verify(userRepository, times(1)).deleteById(2L);
    }
}
