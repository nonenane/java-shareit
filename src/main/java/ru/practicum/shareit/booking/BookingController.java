package ru.practicum.shareit.booking;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.state.BookingState;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @PostMapping()
    public BookingDto createBooking(@Valid @RequestBody BookingDto bookingDto,
                                    @RequestHeader("X-Sharer-User-Id") Long id) {


        Booking booking = bookingService.createBooking(BookingMapper.toBooking(bookingDto), bookingDto.getItemId(), id);
        log.info("Выполнен запрос createBooking");
        return BookingMapper.toBookingDto(booking);
    }


    @PatchMapping("{bookingId}")
    public BookingDto confirmBooking(@PathVariable Long bookingId,
                                     @RequestParam Boolean approved,
                                     @RequestHeader("X-Sharer-User-Id") Long id) {


        Booking booking = bookingService.confirmBooking(bookingId, approved, id);
        log.info("Выполнен запрос confirmBooking");
        return BookingMapper.toBookingDto(booking);
    }


    @GetMapping("{bookingId}")
    public BookingDto getBooking(@PathVariable Long bookingId,
                                 @RequestHeader("X-Sharer-User-Id") Long id) {
        Booking booking = bookingService.getBooking(bookingId, id);
        log.info("Выполнен запрос getBooking");
        return BookingMapper.toBookingDto(booking);
    }


    @GetMapping
    public List<BookingDto> getAllMyBookings(@RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                             @RequestHeader("X-Sharer-User-Id") Long id) {

        List<Booking> bookingList = bookingService.getAllMyBookings(id, state);
        log.info("Выполнен запрос getAllMyBookings");
        return bookingList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForMyItems(@RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                                     @RequestHeader("X-Sharer-User-Id") Long id) {

        List<Booking> bookingList = bookingService.getAllBookingsForMyItems(id, state);
        log.info("Выполнен запрос getAllBookingsForMyItems");
        return bookingList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}
