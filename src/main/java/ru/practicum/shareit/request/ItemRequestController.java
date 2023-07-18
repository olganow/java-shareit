package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.util.List;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping()
    public ItemRequestDto createRequest(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                        @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return requestService.createRequest(id, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItems getRequestById(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                               @PathVariable Long requestId) {
        return requestService.getRequestById(id, requestId);
    }

    @GetMapping()
    public List<ItemRequestWithItems> getOwnRequests(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) throws ValidationException {
        return requestService.getOwnRequests(id, from, size);
    }

    @GetMapping("/all")
    public List<ItemRequestWithItems> getAllRequests(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) throws ValidationException {
        return requestService.getAll(id, from, size);
    }

}
