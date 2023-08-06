package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;


import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping()
    public ItemRequestDto createRequest(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                        @RequestBody @Valid ItemRequestShortDto itemRequestShortDto) {
        log.debug("Create request");
        return requestService.createRequest(id, itemRequestShortDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                         @PathVariable Long requestId) {
        log.debug("Get requests by id = {}", id);
        return requestService.getRequestById(id, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Get all requests");
        return requestService.getAllRequests(id, from, size);
    }

    @GetMapping()
    public List<ItemRequestDto> getAllRequestsByRequester(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                                          @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                          @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Get all requests by requester ");
        return requestService.getAllRequestsByRequester(id, from, size);
    }

}
