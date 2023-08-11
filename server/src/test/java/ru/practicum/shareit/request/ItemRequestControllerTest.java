package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.user.User;

import javax.validation.ValidationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestService itemRequestService;
    private ItemRequestDto itemRequestDto;
    private ItemRequestDto itemRequestDtoAnother;
    private ItemRequestShortDto itemRequestShortDto;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @BeforeEach
    void beforeEach() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .name("User_Name")
                .email("user@test.testz")
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .description("Item request description")
                .requesterId(user.getId())
                .created(Timestamp.valueOf(now))
                .build();

        itemRequestDtoAnother = ItemRequestDto.builder()
                .id(2L)
                .description("Description")
                .created(Timestamp.valueOf(now))
                .items(List.of())
                .build();

        itemRequestShortDto = ItemRequestShortDto.builder()
                .description("Item request description")
                .build();
    }

    @Test
    public void createItemRequestTest() throws Exception {
        when(itemRequestService.createRequest(anyLong(), any())).thenReturn(itemRequestDto);
        String result = mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestDto), result);

    }


    @Test
    public void createItemRequestWithWrongUserValidationTest() throws Exception {
        when(itemRequestService.createRequest(anyLong(), any()))
                .thenThrow(new NotFoundException("Объект не найден {}"));
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 999)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createRequestWithEmptyDescriptionValidationTest() throws Exception {
        itemRequestShortDto.setDescription("");
        when(itemRequestService.createRequest(anyLong(), any()))
                .thenThrow(new ValidationException("Описание не может быть пустым"));
        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestShortDto))
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequestByIdTest() throws Exception {
        when(itemRequestService.getRequestById(anyLong(), anyLong())).thenReturn(itemRequestDtoAnother);

        String result = mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(itemRequestDtoAnother), result);
    }

    @Test
    void getRequestByIdWithWrongIdValidationTest() throws Exception {
        when(itemRequestService.getRequestById(anyLong(), anyLong()))
                .thenThrow(new NotFoundException("Item Request hasn't found"));

        mockMvc.perform(get("/requests/{requestId}", 999L)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void getRequestByIdWithWrongUserIdValidationTest() throws Exception {
        when(itemRequestService.getRequestById(anyLong(), anyLong()))
                .thenThrow(new NotFoundException("Item Request hasn't found"));

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 999))
                .andExpect(status().isNotFound());
    }


    @Test
    void getAllRequestsByRequesterTest() throws Exception {
        List<ItemRequestDto> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequestDtoAnother);
        when(itemRequestService.getAllRequestsByRequester(anyLong(), anyInt(), anyInt())).thenReturn(itemRequests);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "10")
                        .contentType("application/json")
                )
                .andExpect(status().isOk());
    }

    @Test
    void getAllByRequester() throws Exception {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "description", userId, null,
                List.of());
        when(itemRequestService.getAllRequestsByRequester(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestDto));
        String result = mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemRequestDto)), result);
    }


}
