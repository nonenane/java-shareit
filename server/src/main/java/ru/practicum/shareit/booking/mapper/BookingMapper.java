package ru.practicum.shareit.booking.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

@NoArgsConstructor
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus(),
                booking.getItem().getId(),
                booking.getBooker().getId());
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItem(),
                bookingDto.getBooker(),
                bookingDto.getStatus());
    }

}