package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.user.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.request.dto.ItemRequestMapper.itemRequestToRequestDto;
import static ru.practicum.shareit.request.dto.ItemRequestMapper.itemRequestToRequestWithItems;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestsRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto createRequest(Long id, ItemRequestShortDto itemRequestShortDto) {
        ItemRequest itemRequest = ItemRequestMapper.itemRequestShortDtoToItemRequest(itemRequestShortDto);

        itemRequest.setRequester(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User hasn't found")));

        itemRequest.setCreated(Timestamp.valueOf(LocalDateTime.now()));

        log.info("Get all user's request with id = {}", id);
        return itemRequestToRequestDto(itemRequestsRepository.save(itemRequest));
    }

    @Override
    public ItemRequestDto getRequestById(Long id, Long requestId) {
        validateUser(id);

        ItemRequest itemRequest = itemRequestsRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request hasn't found"));

        ItemRequestDto request = itemRequestToRequestWithItems(itemRequest);
        List<ItemDto> items = itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemMapper::itemToItemDto).collect(Collectors.toList());

        request.setItems(items.isEmpty() ? new ArrayList<>() : items);
        log.info("Get request by id = {}", requestId);
        return request;

    }

    public List<ItemRequestDto> getAllRequestsByRequester(Long id, int from, int size) {
        validateUser(id);
        Pageable pageable = PageRequest.of(from, size, Sort.by("created").descending());

        List<ItemRequest> itemRequests = itemRequestsRepository.findAllByRequesterId(id, pageable);

        log.info("Get requests by requester with id = {}", id);
        return getItemRequestsDtoWithItems(itemRequests);
    }


    public List<ItemRequestDto> getAllRequests(Long id, int from, int size) {
        validateUser(id);
        Pageable pageable = PageRequest.of(from, size, Sort.by("created").descending());

        List<ItemRequest> itemRequests = itemRequestsRepository.findAllByRequesterIdNot(id, pageable);

        log.info("Get all requests");
        return getItemRequestsDtoWithItems(itemRequests);
    }

    private List<ItemRequestDto> getItemRequestsDtoWithItems(List<ItemRequest> itemRequests) {
        List<ItemRequestDto> itemRequestDto = new ArrayList<>();
        List<Long> requests = itemRequests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());

        List<Item> itemsByRequests = itemRepository.findByRequest_IdIn(requests);

        Map<ItemRequest, List<Item>> itemRequestsByItem = itemsByRequests.stream()
                .collect(groupingBy(Item::getRequest, toList()));

        for (ItemRequest itemRequest : itemRequests) {
            List<Item> items = itemRequestsByItem.getOrDefault(itemRequest, List.of());

            List<ItemDto> itemDtos = items.stream()
                    .map(ItemMapper::itemToItemDto)
                    .collect(toList());
            itemRequestDto.add(ItemRequestMapper.toItemRequestDto(itemRequest, itemDtos));
        }
        return itemRequestDto;
    }

    private void validateUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with id= " + id + " hasn't not found");
        }
    }
}