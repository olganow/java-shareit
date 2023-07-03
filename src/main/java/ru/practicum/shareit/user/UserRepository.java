package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Long userId);

    User removeUserById(Long userId);

    Boolean isEmailExisted(String email);
}
