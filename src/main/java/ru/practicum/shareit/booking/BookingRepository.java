package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(Long bookerId, Sort sort);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime dateTime,
                                                             LocalDateTime dateTime1, Sort sort);

    List<Booking> findAllByBookerIdAndEndBefore(Long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByBookerIdAndStartAfter(Long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, Status status, Sort sort);

    Boolean existsByBookerIdAndEndBeforeAndStatus(Long bookerId, LocalDateTime localDateTime, Status status);

    List<Booking> findAllByItemOwnerId(Long ownerId, Sort sort);

    List<Booking> findAllByItemOwnerIdAndEndBefore(Long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime dateTime,
                                                                LocalDateTime anotherDateTime, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartAfter(Long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStatus(Long bookerId, Status status, Sort sort);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(Long itemId, LocalDateTime localDate,
                                                                               Status status);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(Long itemId, LocalDateTime localDate,
                                                                             Status status);

    Optional<Booking> findFirstByItemIdInAndStartLessThanEqualAndStatus(List<Long> idItems, LocalDateTime now,
                                                                        Status approved, Sort sort);

    Optional<Booking> findFirstByItemIdInAndStartAfterAndStatus(List<Long> idItems, LocalDateTime now,
                                                                Status approved, Sort sort);

}
