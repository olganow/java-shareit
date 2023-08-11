package ru.practicum.shareitgateway.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareitgateway.client.BaseClient;
import ru.practicum.shareitgateway.item.dto.CommentDto;
import ru.practicum.shareitgateway.item.dto.ItemDto;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(Long userId, ItemDto requestDto) {
        log.info("Create item for user with id = {} ", userId);
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getItemById(Long itemId, Long userId) {
        log.info("Get item with id = {} for user with id = {} ", itemId, userId);
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItems(Long userId) {
        log.info("Get items for user with id = {} ", userId);
        return get("", userId);
    }

    public ResponseEntity<Object> updateItem(ItemDto requestDto, Long itemId, Long userId) {
        log.info("Update item with id = {} for user with id = {} ", itemId, userId);
        return patch("/" + itemId, userId, requestDto);
    }

    public ResponseEntity<Object> searchItemByText(Long userId, String text) {
        if (text.isBlank()) return ResponseEntity.ok(List.of());
        Map<String, Object> parameters = Map.of("text", text);
        log.info("Find item with text = {}", text);
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> removeItem(Long itemId) {
        log.info("Remove item with id = {}", itemId);
        return delete("/" + itemId);
    }


    public ResponseEntity<Object> createComment(Long itemId, long userId, CommentDto requestDto) {
        log.info("Create comment item with id = {}", itemId);
        return post("/" + itemId + "/comment", userId, requestDto);
    }
}
