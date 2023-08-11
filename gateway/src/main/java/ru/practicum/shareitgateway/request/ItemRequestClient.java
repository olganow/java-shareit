package ru.practicum.shareitgateway.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareitgateway.client.BaseClient;
import ru.practicum.shareitgateway.request.dto.ItemRequestDto;


import java.util.Map;

@Service
@Slf4j
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItemRequest(long userId, ItemRequestDto itemRequestDto) {
        log.info("Create ite, request {}", userId);
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> getItemRequest(Long requestId, long userId) {
        log.info("Get request with id = {}", requestId);
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> getAllItemRequest(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size);
        log.info("Get all item requests for user with id =  {}", userId);
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getItemRequestsByUser(long userId) {
        log.info("Get all  for user with id =  {}", userId);
        return get("", userId);
    }

}
