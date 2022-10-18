package ru.practicum.shareit.booking;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @MockBean
    BookingService bookingService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;


    User user = new User(1L, "user", "user@email.ru");
    User booker = new User(2L, "booker", "booker@email.ru");
    Item item = new Item(1L, user.getId(), "item", "item desc", true, null);
    Booking booking = new Booking(null,
            LocalDateTime.now().plusMinutes(1),
            LocalDateTime.now().plusDays(1),
            item,
            booker,
            BookingStatus.WAITING);
    Booking bookingWithId = new Booking(1L,
            LocalDateTime.now().plusMinutes(1),
            LocalDateTime.now().plusDays(1),
            item,
            booker,
            BookingStatus.WAITING);
    BookingDto bookingDto = BookingMapper.toBookingDto(booking);
    BookingDto bookingDtoWithId = BookingMapper.toBookingDto(bookingWithId);

    @Test
    void createBooking() throws Exception {

        when(bookingService.createBooking(Mockito.any(Booking.class), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(bookingDtoWithId));


        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.itemId", is(1)))
                .andExpect(jsonPath("$.bookerId", is(2)));
    }

    @Test
    void confirmBooking() throws Exception {

        when(bookingService.confirmBooking(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(bookingDtoWithId));

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.itemId", is(1)))
                .andExpect(jsonPath("$.bookerId", is(2)));
    }

    @Test
    void getBooking() throws Exception {

        when(bookingService.getBooking(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(bookingDtoWithId));

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.itemId", is(1)))
                .andExpect(jsonPath("$.bookerId", is(2)));
    }

    @Test
    void getAllMyBookings() throws Exception {
        when(bookingService.getAllMyBookings(Mockito.anyLong(),
                Mockito.any(BookingState.class),
                Mockito.any(),
                Mockito.any()))
                .thenReturn(List.of(bookingDtoWithId));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].itemId", is(1)))
                .andExpect(jsonPath("$.[0].bookerId", is(2)));
    }

    @Test
    void getAllBookingsForMyItems() throws Exception {
        when(bookingService.getAllBookingsForMyItems(Mockito.anyLong(),
                Mockito.any(BookingState.class),
                Mockito.any(),
                Mockito.any()))
                .thenReturn(List.of(bookingDtoWithId));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].itemId", is(1)))
                .andExpect(jsonPath("$.[0].bookerId", is(2)));
    }
}