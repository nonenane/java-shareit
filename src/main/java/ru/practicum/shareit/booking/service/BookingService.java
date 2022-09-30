package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.state.BookingState;

import java.util.List;
import java.util.Optional;


public interface BookingService {
    Optional<BookingDto> createBooking(Booking booking, Long itemId, Long bookerId);

    Optional<BookingDto> confirmBooking(Long bookingId, Boolean approved, Long requestorId);


    Optional<BookingDto> getBooking(Long bookingId, Long requestorId);


    List<BookingDto> getAllMyBookings(Long bookerId, BookingState state);


    List<BookingDto> getAllBookingsForMyItems(Long ownerId, BookingState state);
}