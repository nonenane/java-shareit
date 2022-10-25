package ru.practicum.shareit.booking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker_IdOrderByStartDesc(Long bookerId);

    List<Booking> findAllByStatusAndBooker_IdOrderByStartDesc(BookingStatus status,
                                                              Long bookerId);


    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId,
                                                                              LocalDateTime start,
                                                                              LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(Long bookerId,
                                                                               LocalDateTime start,
                                                                               LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStartAfterAndEndAfterOrderByStartDesc(Long bookerId,
                                                                             LocalDateTime start,
                                                                             LocalDateTime end);

    @Query(value = "select b.booking_id, b.start_time, b.end_time, b.item_id, b.booker_id, b.status " +
            "from bookings as b " +
            "left join items as i on i.id = b.item_id " +
            "where i.owner_id = ?1 ", nativeQuery = true)
    List<Long> findUserItemsBookingsIds(Long ownerId);

    List<Booking> findAllByItem_IdOrderByStartDesc(Long itemId);
}
