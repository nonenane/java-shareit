package ru.practicum.shareit.booking;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.state.BookingState;

import javax.validation.Valid;
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
                                             @RequestHeader("X-Sharer-User-Id") Long id,
                                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(required = false, defaultValue = "100") @Min(1) Integer size) {

        log.info("Выполнен запрос getAllMyBookings");
        return bookingService.getAllMyBookings(id, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForMyItems(@RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                                     @RequestHeader("X-Sharer-User-Id") Long id,
                                                     @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(required = false, defaultValue = "100") @Positive Integer size) {

        log.info("Выполнен запрос getAllBookingsForMyItems");
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return bookingService.getAllBookingsForMyItems(id, state, from, size);
    }
}
