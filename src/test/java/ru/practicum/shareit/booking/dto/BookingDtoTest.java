package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.status.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testBookingDto() throws Exception {
        LocalDateTime start = LocalDateTime.of(1999, 12, 20, 3, 43, 34);
        LocalDateTime end = start.plusDays(1);


        BookingDto bookingDto = new BookingDto(1L,
                start,
                end,
                null,
                null,
                BookingStatus.WAITING,
                3L,
                5L);

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.start").isEqualTo("1999-12-20T03:43:34");
        assertThat(result).extractingJsonPathValue("$.end").isEqualTo("1999-12-21T03:43:34");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(3);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(5);
    }

}