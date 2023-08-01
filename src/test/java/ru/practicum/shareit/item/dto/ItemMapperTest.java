package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ItemMapperTest {
    private Item item;
    private ItemDto itemDto;
    private ItemShortDto itemShortDto;
    private User user;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("User_Name")
                .email("username@test.testz")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Item_name")
                .description("Item_description")
                .available(true)
                .owner(user)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("Item_name")
                .description("Item_description")
                .available(true)
                .owner(new ItemDto.Owner(user.getId(), user.getName(), user.getEmail()))
                .build();

        itemShortDto = ItemShortDto
                .builder()
                .id(1L)
                .name("Item_name")
                .description("Item_description")
                .available(true)
                .requestId(1L)
                .build();
    }

    @Test
    void itemToItemDtoTest() {
        itemDto = ItemMapper.itemToItemDto(item);

        assertThat(itemDto.getId()).isEqualTo(1L);
        assertThat(itemDto.getName()).isEqualTo("Item_name");
        assertThat(itemDto.getDescription()).isEqualTo("Item_description");
        assertThat(itemDto.getAvailable()).isEqualTo(true);
        assertThat(itemDto.getComments()).isEqualTo(List.of());
        assertThat(itemDto.getRequestId()).isEqualTo(null);

    }

    @Test
    void itemDtoToItemTest() {

        item = ItemMapper.itemDtoToItem(itemDto);

        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getName()).isEqualTo("Item_name");
        assertThat(item.getDescription()).isEqualTo("Item_description");
        assertThat(item.getAvailable()).isEqualTo(true);
        assertThat(item.getOwner()).isEqualTo(user);
        assertThat(item.getRequest()).isEqualTo(null);
    }

    @Test
    void itemShortDtoToItemDtoTest() {
        itemDto = ItemMapper.itemShortDtoToItemDto(itemShortDto);

        assertThat(itemDto.getId()).isEqualTo(1L);
        assertThat(this.itemDto.getName()).isEqualTo("Item_name");
        assertThat(this.itemDto.getDescription()).isEqualTo("Item_description");
        assertThat(itemDto.getAvailable()).isEqualTo(true);
        assertThat(itemDto.getComments()).isEqualTo(List.of());
    }
}