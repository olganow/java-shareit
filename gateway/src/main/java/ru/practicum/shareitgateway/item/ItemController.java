package ru.practicum.shareitgateway.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.item.dto.CommentDto;
import ru.practicum.shareitgateway.item.dto.ItemDto;
import ru.practicum.shareitgateway.util.Marker;

import javax.validation.Valid;

import static ru.practicum.shareitgateway.util.Constants.REQUEST_HEADER_USER_ID;

@Controller
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                             @Validated(Marker.OnCreate.class) @Valid @RequestBody ItemDto requestDto) {
        log.info("Create item");
        return itemClient.createItem(userId, requestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable Long id,
                                              @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        log.info("Get item with id = {}", id);
        return itemClient.getItemById(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        log.info("Get items {}", userId);
        return itemClient.getItems(userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@Validated(Marker.OnUpdate.class) @RequestBody ItemDto requestDto,
                                             @PathVariable Long id,
                                             @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        log.info("Update item with id = {}", id);
        return itemClient.updateItem(requestDto, id, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemByText(@RequestParam String text,
                                                   @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        log.info("Search item by text {} ", text);
        return itemClient.searchItemByText(userId, text);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeItem(@PathVariable Long id) {
        log.info("Remove item with id = {}", id);
        return itemClient.removeItem(id);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long itemId,
                                                @RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                @RequestBody CommentDto requestDto) {
        log.info("Create comment for item with id = {}", itemId);
        return itemClient.createComment(itemId, userId, requestDto);
    }
}
