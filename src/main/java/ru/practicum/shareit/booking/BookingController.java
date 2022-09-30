package ru.practicum.shareit.booking;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.state.BookingState;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @PostMapping()
    public Optional<BookingDto> createBooking(@Valid @RequestBody BookingDto bookingDto,
                                              @RequestHeader("X-Sharer-User-Id") Long id) {

        log.info("Выполнен запрос createBooking");
        return bookingService.createBooking(BookingMapper.toBooking(bookingDto), bookingDto.getItemId(), id);

    }


    @PatchMapping("{bookingId}")
    public Optional<BookingDto> confirmBooking(@PathVariable Long bookingId,
                                               @RequestParam Boolean approved,
                                               @RequestHeader("X-Sharer-User-Id") Long id) {

        log.info("Выполнен запрос confirmBooking");
        return bookingService.confirmBooking(bookingId, approved, id);

    }


    @GetMapping("{bookingId}")
    public Optional<BookingDto> getBooking(@PathVariable Long bookingId,
                                           @RequestHeader("X-Sharer-User-Id") Long id) {
        log.info("Выполнен запрос getBooking");
        return bookingService.getBooking(bookingId, id);

    }


    @GetMapping
    public List<BookingDto> getAllMyBookings(@RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                             @RequestHeader("X-Sharer-User-Id") Long id) {

        log.info("Выполнен запрос getAllMyBookings");
        return bookingService.getAllMyBookings(id, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForMyItems(@RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                                     @RequestHeader("X-Sharer-User-Id") Long id) {

        log.info("Выполнен запрос getAllBookingsForMyItems");
        return bookingService.getAllBookingsForMyItems(id, state);
    }
}
