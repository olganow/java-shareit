package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
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
    private final ItemRepository itemRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userDtotoUser(userDto);
        log.info("User with email = {}  has been created", userDto.getEmail());
        try {
            return userToUserDto(repository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException(String.format(
                    "User %s has been registered yet", userDto.getEmail()
            ));
        }
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
        try {
            return userToUserDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Already exists");
        }
    }

    @Override
    public void removeUserById(Long id) {
        repository.deleteById(id);
    }
}
