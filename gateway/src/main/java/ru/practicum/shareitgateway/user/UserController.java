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
@Validated
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Validated(Marker.OnCreate.class)
                                             UserRequestDto userRequestDto) {
        log.info("Create user {}", userRequestDto);
        return userClient.createUser(userRequestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        log.info("Get user with id = {}", id);
        return userClient.getUserById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Get all users");
        return userClient.getUsers();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUserById(@Validated(Marker.OnUpdate.class)
                                                 @RequestBody UserRequestDto userRequestDto,
                                                 @PathVariable Long id) {
        log.info("Update user with id = {}", id);
        return userClient.updateUserById(id, userRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeUserById(@PathVariable Long id) {
        log.info("Remove user with id = {}", id);
        return userClient.removeUserById(id);
    }
}
