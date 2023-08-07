package ru.practicum.shareitgateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.user.dto.UserRequestDto;
import ru.practicum.shareitgateway.util.Marker;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Validated(Marker.OnCreate.class) UserRequestDto userRequestDto) {
        log.info("Create user {}", userRequestDto);
        return userClient.createUser(userRequestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info("Get user with id = {}", userId);
        return userClient.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Get all users");
        return userClient.getUsers();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUserById(@Validated(Marker.OnUpdate.class)
                                                 @RequestBody UserRequestDto userRequestDto,
                                                 @PathVariable Long userId) {
        log.info("Update user with id = {}", userId);
        return userClient.updateUserById(userId, userRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeUserById(@PathVariable Long userId) {
        log.info("Remove user with id = {}", userId);
        return userClient.removeUserById(userId);
    }
}
