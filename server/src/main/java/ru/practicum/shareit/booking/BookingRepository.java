package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(Long bookerId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime dateTime,
                                                             LocalDateTime dateTime1, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndBefore(Long bookerId, LocalDateTime dateTime, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartAfter(Long bookerId, LocalDateTime dateTime, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, Status status, Pageable pageable);

    Boolean existsByBookerIdAndEndBeforeAndStatus(Long bookerId, LocalDateTime localDateTime, Status status);

    List<Booking> findAllByItemOwnerId(Long ownerId, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndEndBefore(Long bookerId, LocalDateTime dateTime, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime dateTime,
                                                                LocalDateTime anotherDateTime, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartAfter(Long bookerId, LocalDateTime dateTime, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStatus(Long bookerId, Status status, Pageable pageable);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(Long itemId, LocalDateTime localDate,
                                                                               Status status);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(Long itemId, LocalDateTime localDate,
                                                                             Status status);

    Optional<Booking> findFirstByItemIdInAndStartLessThanEqualAndStatus(List<Long> idItems, LocalDateTime now,
                                                                        Status approved, Sort sort);

    Optional<Booking> findFirstByItemIdInAndStartAfterAndStatus(List<Long> idItems, LocalDateTime now,
                                                                Status approved, Sort sort);

}
