package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User with id =" + id + "doesn't exist");
        }
        log.info("User with id = {} is uploaded from Repository", id);
        return users.get(id);
    }

    @Override
    public User removeUserById(Long id) {
        User memoryUser = getUserById(id);
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