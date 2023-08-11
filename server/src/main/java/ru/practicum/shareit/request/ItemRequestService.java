package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;


import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createRequest(Long id, ItemRequestShortDto itemRequestShortDto);

    ItemRequestDto getRequestById(Long id, Long requestId);

    List<ItemRequestDto> getAllRequests(Long id, int from, int size);

    List<ItemRequestDto> getAllRequestsByRequester(Long userId, int from, int size);

}
