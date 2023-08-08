package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingServiceImpl bookingService;
    private final LocalDateTime start = LocalDateTime.now().plusDays(1);
    private final LocalDateTime end = start.plusDays(2);

    @SneakyThrows
    @Test
    void createBookingTest() {
        Long userId = 1L;
        Long itemId = 1L;
        BookingShortDto createBookingDto = new BookingShortDto(userId, start, end, itemId);
        BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
        when(bookingService.createBooking(anyLong(), any())).thenReturn(bookingDto);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void getByIdTest() {
        Long bookingId = 1L;
        BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(bookingDto);

        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void getAllByOwnerTest() {
        BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
        when(bookingService.getAllOwnersBookingByState(anyLong(), any(), anyInt(),
                anyInt())).thenReturn(List.of(bookingDto));
        String result = mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @SneakyThrows
    @Test
    void getAllBookingByStateTest() {
        BookingDto bookingDto = new BookingDto(1L, start, end, Status.WAITING, null, null);
        when(bookingService.getAllBookingByState(anyLong(), any(), anyInt(),
                anyInt())).thenReturn(List.of(bookingDto));
        String result = mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @SneakyThrows
    @Test
    void approveBookingTest() {
        Long bookingId = 1L;
        BookingDto bookingDto = new BookingDto(bookingId, start, end, Status.WAITING, null, null);
        when(bookingService.approveBooking(anyLong(), anyLong(),
                anyBoolean())).thenReturn(bookingDto);
        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", 1)
                        .param("bookingId", "1L")
                        .param("approved", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }
}
