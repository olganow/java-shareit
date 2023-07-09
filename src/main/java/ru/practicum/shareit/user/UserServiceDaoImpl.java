package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceDaoImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final ItemRepository itemRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.userDtotoUser(userDto);
        log.info("User with email = {}  has been created", userDto.getEmail());
        return userMapper.userToUserDto(repository.save(user));
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Get all user");
        return List.copyOf(repository.findAll());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() ->
                new NotFoundException("User with id = "+ id +" has not found"));
        log.info("User with id = {} is uploaded", id);
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto updateUserById(Long id, UserDto userDto) {
        Optional<User> user = repository.findById(id);
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.get().setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            user.get().setEmail(userDto.getEmail());
        }
        log.info("User with id = {} is updated", userDto.getId());
        return userMapper.userToUserDto(repository.save(user.get()));
    }

    @Override
    public Boolean removeUserById(Long id) {
        if (repository.existsById(id)) {
            itemRepository.deleteAll(itemRepository.findAllByOwnerId(id));
            repository.deleteById(id);
        }
        log.info("User with id = {} removed", id);
        return !repository.existsById(id);
    }
}
