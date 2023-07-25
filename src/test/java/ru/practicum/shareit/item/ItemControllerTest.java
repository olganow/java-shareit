package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;
    private ItemDto itemDto;
    private User user;
    private Item item;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("User_WithBooking_Name")
                .email("userwithbooking@test.testz")
                .build();

        itemDto = ItemDto.builder()
                .name("Item_name")
                .description("Item_description")
                .owner(new ItemDto.Owner(user.getId(), user.getName(), user.getEmail()))
                .available(true)
                .build();

        item = Item.builder()
                .id(1L)
                .name("Item_name")
                .description("Item_description")
                .available(true)
                .owner(user)
                .build();
    }

    @Test
    public void createItemTest() throws Exception {


        when(itemService.createItem(any(), anyLong())).thenReturn(itemDto);
        String result = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @Test
    public void shouldNotCreateItemWitWrongUserValidationTest() throws Exception {
        when(itemService.createItem(any(), anyLong())).thenThrow(new NotFoundException("The user doesn't exist"));
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 999)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createItemWithEmptyDescriptionValidationTest() throws Exception {
        when(itemService.createItem(any(), anyLong())).thenThrow(new ValidationException("The description can't be empty"));
        itemDto.setDescription("");
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createItemWithEmptyNameValidationTest() throws Exception {
        when(itemService.createItem(any(), anyLong())).thenThrow(new ValidationException("The name can't be empty"));
        itemDto.setName("");
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getItemByIdTest() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemDto);
        String result = mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    void getItemWithWrongUserValidationTest() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong())).thenThrow(new NotFoundException("The User doesn't exist"));
        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    void getItemByWrongIdValidationTest() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong())).thenThrow(new NotFoundException("The Id doesn't exist"));
        mockMvc.perform(get("/items/{itemId}", 99L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllItemsTest() throws Exception {
        List<ItemDto> items = new ArrayList<>();
        items.add(itemDto);
        when(itemService.getAllItems(anyLong())).thenReturn(items);
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())));
    }

    @Test
    public void updateItemByIdTest() throws Exception {
        when(itemService.updateItemById(any(), anyLong(), any())).thenReturn(itemDto);
        itemDto.setName("New_Name");
        itemDto.setDescription("New_description");
        String result = mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @Test
    public void searchItemByTextTest() throws Exception {
        when(itemService.searchItemByText(anyString())).thenReturn(List.of(itemDto));
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "name")
                        .param("from", "0")
                        .param("size", "10"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andReturn()
                .getResponse()
                .getContentAsString();


    }

    @Test
    public void addCommentTest() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .text("Comment text")
                .build();
        when(itemService.addComment(any(), anyLong(), anyLong())).thenReturn(commentDto);
        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.created", is(commentDto.getCreated())));
    }

    @Test
    public void addEmptyCommentValidationTest() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .text("")
                .build();
        when(itemService.addComment(any(), anyLong(), anyLong())).thenThrow(new ValidationException("Отсутствует текст поискового запроса"));
        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCommentWithNotFoundItemValidationTest() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .text("Comment_text")
                .build();
        when(itemService.addComment(any(), anyLong(), anyLong())).thenThrow(new NotFoundException("Объект не найден {}"));
        mockMvc.perform(post("/items/{itemId}/comment", 99L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addCommentWithNotFoundUserValidationTest() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .text("Comment text")
                .build();
        when(itemService.addComment(any(), anyLong(), anyLong())).thenThrow(new NotFoundException("Объект не найден {}"));
        mockMvc.perform(post("/items/{itemId}/comment", 99L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCommentsTest() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemDto);
        String result = mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }
}