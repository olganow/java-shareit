package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.UserMapper.userDtotoUser;
import static ru.practicum.shareit.user.UserMapper.userToUserDto;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceDaoImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userDtotoUser(userDto);
        log.info("User with email = {}  has been created", userDto.getEmail());
        return userToUserDto(repository.save(user));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers() {
        log.info("Get all user");
        return repository.findAll()
                .stream()
                .map(UserMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() ->
                new NotFoundException("User with id = " + id + " has not found"));
        log.info("User with id = {} is uploaded", id);
        return userToUserDto(user);
    }

    @Override
    public UserDto updateUserById(Long id, UserDto userDto) {
        User user = repository.findById(id).orElseThrow(() -> new NotFoundException("Error"));
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            user.setEmail(userDto.getEmail());
        }
        log.info("User with id = {} is updated", userDto.getId());
        repository.saveAndFlush(user);
        return userToUserDto(user);
    }

    @Override
    public void removeUserById(Long id) {
        repository.deleteById(id);
    }
}
