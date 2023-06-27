package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.marker.Marker;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Validated({Marker.OnCreate.class}) @RequestBody UserDto userDto) {
        log.info("The user with id = {} has been created", userDto.getId());
        return userService.createUser(userDto);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.info("Get a user by id = {}", id);
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("GET {} users", userService.getAllUsers().size());
        return userService.getAllUsers();
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public UserDto updateUserById(@Validated(Marker.OnUpdate.class) @RequestBody UserDto userDto,
                                  @PathVariable Long id) {
        log.info("The user with id = {} has been updated", userDto.getId());
        return userService.updateUserById(id, userDto);
    }

    @DeleteMapping("/{id}")
    public UserDto removeUserById(@PathVariable Long id) {
        log.info("The user with id = {} has been removed", id);
        return userService.removeUserById(id);
    }
}
