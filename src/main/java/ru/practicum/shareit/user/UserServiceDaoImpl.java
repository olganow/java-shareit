package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceDaoImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.userDtotoUser(userDto);
        if (repository.isEmailExisted(user.getEmail())) {
            throw new ValidateException("User with email = " + userDto.getEmail() + " exists");
        }
        log.info("User with email = {}  has been created", userDto.getEmail());
        return userMapper.userToUserDto(repository.createUser(user));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : repository.getAllUsers()) {
            usersDto.add(userMapper.userToUserDto(user));
        }
        log.info("Get all user");
        return usersDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = repository.getUserById(id);
        log.info("User with id = {} is uploaded", id);
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto updateUserById(Long id, UserDto userDto) {
        User user = repository.getUserById(id);
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            if (repository.isEmailExisted(userDto.getEmail())) {
                if (!userDto.getEmail().equals(user.getEmail())) {
                    log.info("User with email with id = {} exists", user.getId());
                    throw new ValidateException("User with email " + userDto.getEmail() + " exists");
                }
            }
            user.setEmail(userDto.getEmail());
        }
        log.info("User with id = {} is updated", user.getId());
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto removeUserById(Long id) {
        User user = repository.removeUserById(id);
        log.info("User with id = {} removed", id);
        return userMapper.userToUserDto(user);
    }
}
