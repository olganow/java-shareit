package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemInRequest;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.user.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Optional<ItemRequest> itemRequestOptional = itemRequestsRepository.findById(requestId);

        if (itemRequestOptional.isPresent()) {
            ItemRequest itemRequest = itemRequestOptional.get();
            log.info("Get requests by id = {}", id);
            return setItemsByItemRequest(itemRequest);
        } else {
            throw new NotFoundException("Request hasn't found");
        }

    }

    @Override
    public List<ItemRequestDto> getAllRequestsByRequester(Long id, int from, int size) {
        validateUser(id);
        Pageable pageable = PageRequest.of(from, size).withSort(Sort.by("created").descending());
        Page<ItemRequestDto> requests = itemRequestsRepository
                .findByRequesterId(id, pageable).map(this::setItemsByItemRequest);

        log.info("Get requests by requester with id = {}", id);
        return requests.stream().collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long id, int from, int size) {
        validateUser(id);
        Pageable pageable = PageRequest.of(from, size).withSort(Sort.by("created").descending());

        log.info("Get all requests");
        return itemRequestsRepository.findAllByRequesterIdNot(id, pageable).map(this::setItemsByItemRequest)
                .stream().collect(Collectors.toList());
    }


    private ItemRequestDto setItemsByItemRequest(ItemRequest itemRequest) {
        ItemRequestDto request = itemRequestToRequestWithItems(itemRequest);
        List<ItemInRequest> items = itemRepository.findAllByRequestId(itemRequest.getId()).stream()
                .map(ItemMapper::itemToItemInRequest).collect(Collectors.toList());

        request.setItems(items.isEmpty() ? new ArrayList<>() : items);

        return request;
    }

    private void validateUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with id= " + id + " hasn't not found");
        }
    }
}