package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.item.dto.ItemMapper.itemDtoToItem;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplIntegrationTest {
    private final ItemService service;

    @MockBean
    private final ItemRepository itemRepository;

    @MockBean
    private final UserRepository userRepository;

    private User user;
    private User userWithBooking;
    private Item item;
    private ItemDto itemDtoOne;
    private ItemDto itemDtoAnother;
    private ItemShortDto itemShortDto;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("User_Name")
                .email("username@test.testz")
                .build();

        userWithBooking = User.builder()
                .id(2L)
                .name("User_WithBooking_Name")
                .email("userwithbooking@test.testz")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Item_name")
                .description("Item_description")
                .available(true)
                .owner(user)
                .build();

        itemDtoOne = ItemDto.builder()
                .id(1L)
                .name("Item name")
                .description("Item description")
                .available(true)
                .owner(new ItemDto.Owner(user.getId(), user.getName(), user.getEmail()))
                .build();

        itemDtoAnother = ItemDto.builder()
                .id(2L)
                .name("Item name")
                .description("Item description")
                .available(true)
                .owner(new ItemDto.Owner(user.getId(), user.getName(), user.getEmail()))
                .build();

        itemShortDto = ItemShortDto.builder()
                .id(1L)
                .name("Item name")
                .description("Item description")
                .available(true)
                .requestId(user.getId())
                .build();

    }

    @Test
    void getAllItemsByOwnerIdTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findAllByOwnerId(anyLong())).thenReturn(List.of(itemDtoToItem(itemDtoOne),
                itemDtoToItem(itemDtoAnother)));
        List<ItemDto> items = service.getAllItems(user.getId());

        assertEquals(2, items.size(), "Item number is not correct");
    }

    @Test
    void getItemByIdTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemDtoToItem(itemDtoOne)));
        ItemDto itemById = service.getItemById(1L, 1L);

        assertEquals(itemDtoOne.getName(), itemById.getName());
    }

    @Test
    void getItemByIdValidationTest() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.getItemById(9999L, 1L));
    }

    @Test
    void createNewItemTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(itemDtoToItem(itemDtoOne));

        ItemDto item = service.createItem(new ItemShortDto(itemDtoOne.getId(), itemDtoOne.getName(),
                itemDtoOne.getDescription(), true, null), user.getId());

        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDtoOne.getName()));
        assertThat(item.getDescription(), equalTo(itemDtoOne.getDescription()));
    }

    @Test
    void createNewItemWithIncorrectUserValidationTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.createItem(itemShortDto, user.getId()));
    }
}
