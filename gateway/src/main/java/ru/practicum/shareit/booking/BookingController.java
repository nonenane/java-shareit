package ru.practicum.shareit.booking;


import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.groupValidate.Create;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingClient bookingClient;

    public BookingController(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }


    @PostMapping()
    public Optional<BookingDto> createBooking(@Validated(Create.class) @RequestBody BookingDto bookingDto,
                                              @RequestHeader("X-Sharer-User-Id") Long id) {

        log.info("Выполнен запрос createBooking");
        return bookingClient.createBooking(bookingDto, id);

    }


    @PatchMapping("{bookingId}")
    public Optional<BookingDto> confirmBooking(@PathVariable Long bookingId,
                                               @RequestParam Boolean approved,
                                               @RequestHeader("X-Sharer-User-Id") Long id) {

        log.info("Выполнен запрос confirmBooking");
        return bookingClient.confirmBooking(bookingId, approved, id);

    }


    @GetMapping("{bookingId}")
    public Optional<BookingDto> getBooking(@PathVariable Long bookingId,
                                           @RequestHeader("X-Sharer-User-Id") Long id) {
        log.info("Выполнен запрос getBooking");
        return bookingClient.getBooking(bookingId, id);

    }


    @GetMapping
    public List<BookingDto> getAllMyBookings(@RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                             @RequestHeader("X-Sharer-User-Id") Long id,
                                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(required = false, defaultValue = "100") @Min(1) Integer size) {

        log.info("Выполнен запрос getAllMyBookings");
        return bookingClient.getAllMyBookings(id, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForMyItems(@RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                                     @RequestHeader("X-Sharer-User-Id") Long id,
                                                     @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(required = false, defaultValue = "100") @Positive Integer size) {

        log.info("Выполнен запрос getAllBookingsForMyItems");
        return bookingClient.getAllBookingsForMyItems(id, state, from, size);
    }
}
