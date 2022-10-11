package ru.practicum.shareit.booking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker_IdOrderByStartDesc(@NotNull Long bookerId);

    List<Booking> findAllByStatusAndBooker_IdOrderByStartDesc(@NotNull BookingStatus status,
                                                              @NotNull Long bookerId);


    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(@NotNull Long bookerId,
                                                                              @NotNull LocalDateTime start,
                                                                              @NotNull LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(@NotNull Long bookerId,
                                                                               @NotNull LocalDateTime start,
                                                                               @NotNull LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStartAfterAndEndAfterOrderByStartDesc(@NotNull Long bookerId,
                                                                             @NotNull LocalDateTime start,
                                                                             @NotNull LocalDateTime end);

    @Query(value = "select b.booking_id, b.start_time, b.end_time, b.item_id, b.booker_id, b.status " +
            "from bookings as b " +
            "left join items as i on i.id = b.item_id " +
            "where i.owner_id = ?1 ", nativeQuery = true)
    List<Long> findUserItemsBookingsIds(@NotNull Long ownerId);

    List<Booking> findAllByItem_IdOrderByStartDesc(@NotNull Long itemId);
}
