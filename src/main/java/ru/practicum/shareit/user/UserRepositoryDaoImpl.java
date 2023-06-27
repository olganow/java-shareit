package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class UserRepositoryDaoImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("User with email = {}  has been added to Repository", user.getEmail());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Get all user from Repository");
        return List.copyOf(users.values());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User with id =" + id + "doesn't exist");
        }
        log.info("User with id = {} is uploaded from Repository", id);
        return Optional.of(users.get(id));
    }


    @Override
    public User updateUserById(User user, Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User with id =" + id + "doesn't exist");
        }
        users.put(id, user);
        log.info("User with id = {} is updated in Repository", id);
        return users.get(id);
    }

    @Override
    public Optional<User> removeUserById(Long id) {
        Optional<User> memoryUser = getUserById(id);
        if (memoryUser.isEmpty()) {
            log.info("User with email with id = {} is not found and can't be removed ", id);
            return Optional.empty();
        }
        users.remove(id);
        log.info("User with id = {} removed", id);
        return memoryUser;
    }

    public final Boolean isEmailExisted(String email) {
        for (User userList : users.values()) {
            if (userList.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}