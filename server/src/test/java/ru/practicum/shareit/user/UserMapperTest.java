package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserMapperTest {

    private User user;
    private UserDto userDto;

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "User_name", "user@test.testz");
        userDto = new UserDto(2L, "UserDto_name", "userdto@test.testz");
    }

    @Test
    void userToUserDtoTest() {
        UserDto userDto = UserMapper.userToUserDto(user);

        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getName()).isEqualTo(user.getName());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void userDtoToUserTest() {
        User user = UserMapper.userDtotoUser(userDto);

        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getName()).isEqualTo(userDto.getName());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    void userDtoToOwnerTest() {
        ItemDto.Owner owner = UserMapper.userDtoToOwner(userDto);

        assertThat(owner.getId()).isEqualTo(userDto.getId());
        assertThat(owner.getName()).isEqualTo(userDto.getName());
        assertThat(owner.getEmail()).isEqualTo(userDto.getEmail());
    }

}
