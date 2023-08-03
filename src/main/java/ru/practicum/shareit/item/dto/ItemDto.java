package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ItemDto {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Owner owner;

    private Long requestId;

    private BookingItemDto lastBooking;

    private BookingItemDto nextBooking;

    private List<CommentDto> comments;

    @Data
    public static class Owner {
        private final Long id;
        private final String name;
        private final String email;

    }

}
