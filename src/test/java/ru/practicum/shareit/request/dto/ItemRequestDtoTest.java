package ru.practicum.shareit.request.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.User;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void itemRequestDtoTest() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .name("User_WithBooking_Name")
                .email("userwithbooking@test.testz")
                .build();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Item_description")
                .created(Timestamp.valueOf(now))
                .requesterId(user.getId())
                .build();
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDto.getDescription());
    }

}