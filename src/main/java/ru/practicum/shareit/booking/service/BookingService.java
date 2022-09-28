package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;


public interface BookingService {
    Booking createBooking(Booking booking, Long itemId, Long bookerId);

    Booking confirmBooking(Long bookingId, Boolean approved, Long requestorId);


    Booking getBooking(Long bookingId, Long requestorId);


    List<Booking> getAllMyBookings(Long bookerId, BookingState state);


    List<Booking> getAllBookingsForMyItems(Long ownerId, BookingState state);
}