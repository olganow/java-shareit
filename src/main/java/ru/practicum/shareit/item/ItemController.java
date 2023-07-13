package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemShortDto itemShortDto,
                              @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        return service.createItem(itemShortDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId,
                               @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        return service.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        return service.getAllItems(userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItemById(@RequestBody ItemShortDto itemShortDto,
                                  @PathVariable Long itemId,
                                  @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        return service.updateItemById(itemShortDto, itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestParam String text) {
        return service.searchItemByText(text);
    }


    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId,
                                 @RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                 @Valid @RequestBody CommentDto commentDto) {
        return service.addComment(commentDto, userId, itemId);
    }
}
