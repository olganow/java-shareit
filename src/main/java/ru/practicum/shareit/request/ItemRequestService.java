package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemList;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createRequest(Long id, ItemRequestShortDto itemRequestShortDto);

    ItemRequestWithItemList getRequestById(Long id, Long requestId);

    List<ItemRequestWithItemList> getAllRequests(Long id, int from, int size);

    List<ItemRequestWithItemList> getAllRequestsByRequester(Long userId, int from, int size);

}
