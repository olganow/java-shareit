package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ItemRequestMapperTest {
    private Item item;
    private User user;
    private ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "User_name", "user@test.testz");
        item = new Item(1L, "Item_name", "Item_Description", true, user,
                null);
        itemRequest = new ItemRequest(1L, "Description", user,
                Timestamp.valueOf(LocalDateTime.now()));
    }

    @Test
    void itemRequestToRequestDtoTest() {
        ItemRequestDto itemRequestDto = ItemRequestMapper.itemRequestToRequestDto(itemRequest);

        assertThat(itemRequestDto.getId()).isEqualTo(itemRequest.getId());
        assertThat(itemRequestDto.getDescription()).isEqualTo(itemRequest.getDescription());
        assertThat(itemRequestDto.getRequesterId()).isEqualTo(itemRequest.getRequester().getId());
        assertThat(itemRequestDto.getCreated()).isEqualTo(itemRequest.getCreated());
    }

    @Test
    void itemRequestShortDtoToItemRequestTest() {
        ItemRequestShortDto itemRequestShortDto = new ItemRequestShortDto(itemRequest.getId(), itemRequest.getDescription());
        ItemRequest itemRequest = ItemRequestMapper.itemRequestShortDtoToItemRequest(itemRequestShortDto);

        assertThat(itemRequest.getDescription()).isEqualTo(itemRequestShortDto.getDescription());
    }

    @Test
    void itemRequestToRequestWithItemsTest() {
        ItemRequestDto itemRequestDto = ItemRequestMapper.itemRequestToRequestWithItems(itemRequest);

        assertThat(itemRequestDto.getId()).isEqualTo(itemRequest.getId());
        assertThat(itemRequestDto.getDescription()).isEqualTo(itemRequest.getDescription());
        assertThat(itemRequestDto.getCreated()).isEqualTo(itemRequest.getCreated());
    }

}
