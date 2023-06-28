package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Long userId);

    void updateUserById(User user, Long userId);

    Optional<User> removeUserById(Long userId);


    Boolean isEmailExisted(String email);
}
