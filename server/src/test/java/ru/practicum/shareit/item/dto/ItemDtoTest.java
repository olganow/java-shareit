/*
package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void itemDtoTest() throws IOException {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Item_name")
                .description("Item_description")
                .available(true)
                .requestId(1L)
                .build();
        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDto.getAvailable());
        assertThat(result).hasEmptyJsonPathValue("$.comments");
        assertThat(result).hasEmptyJsonPathValue("$.lastBooking");
        assertThat(result).hasEmptyJsonPathValue("$.nextBooking");
    }
}*/
