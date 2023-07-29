package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ItemRequestMapperTest {
    private Item item;
    private User user;
    private Comment comment;
    private final LocalDateTime start = LocalDateTime.now();
    ItemRequest itemRequest;

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
        ItemRequestDto ItemRequest = ItemRequestMapper.itemRequestToRequestDto(itemRequest);

        assertThat(ItemRequest.getId()).isEqualTo(itemRequest.getId());
        assertThat(ItemRequest.getDescription()).isEqualTo(itemRequest.getDescription());
        assertThat(ItemRequest.getRequester()).isEqualTo(itemRequest.getRequester());
        assertThat(ItemRequest.getCreated()).isEqualTo(itemRequest.getCreated());
    }

    @Test
    void itemRequestShortDtoToItemRequestTest() {
        ItemRequestShortDto itemRequestShortDto = new ItemRequestShortDto(itemRequest.getId(),
                itemRequest.getDescription(), itemRequest.getRequester().getId(), itemRequest.getCreated());
        ItemRequest ItemRequest = ItemRequestMapper.itemRequestShortDtoToItemRequest(itemRequestShortDto);

        assertThat(ItemRequest.getDescription()).isEqualTo(itemRequestShortDto.getDescription());
        assertThat(ItemRequest.getCreated()).isEqualTo(itemRequestShortDto.getCreated());
    }

    @Test
    void itemRequestToRequestWithItemsTest() {
        ItemRequestWithItemList ItemRequest = ItemRequestMapper.itemRequestToRequestWithItems(itemRequest);

        assertThat(ItemRequest.getId()).isEqualTo(itemRequest.getId());
        assertThat(ItemRequest.getDescription()).isEqualTo(itemRequest.getDescription());
        assertThat(ItemRequest.getCreated()).isEqualTo(itemRequest.getCreated());
    }


}