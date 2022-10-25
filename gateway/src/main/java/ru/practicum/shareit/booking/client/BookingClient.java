package ru.practicum.shareit.booking.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingClient {

    private final WebClient client;
    private final String serverUri;

    public BookingClient(@Value("${shareit-server.url}") String serverUri) {
        this.serverUri = serverUri;
        this.client = WebClient.create();
    }

    public Optional<BookingDto> createBooking(BookingDto bookingDto, Long requetorId) {
        return client.post()
                .uri(serverUri + "/bookings")
                .body(Mono.just(bookingDto), BookingDto.class)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToMono(BookingDto.class)
                .blockOptional();
    }

    public Optional<BookingDto> confirmBooking(Long bookingId, Boolean approved, Long requetorId) {
        return client.patch()
                .uri(serverUri + "/bookings/" + bookingId
                        + "/?approved=" + approved)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToMono(BookingDto.class)
                .blockOptional();
    }

    public Optional<BookingDto> getBooking(Long bookingId, Long requetorId) {
        return client.get()
                .uri(serverUri + "/bookings/" + bookingId)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToMono(BookingDto.class)
                .blockOptional();
    }

    public List<BookingDto> getAllMyBookings(Long requetorId,
                                             BookingState state,
                                             Integer from,
                                             Integer size) {
        return client.get()
                .uri(serverUri + "/bookings/"
                        + "?state=" + state
                        + "&from=" + from
                        + "&size=" + size)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToFlux(BookingDto.class)
                .collect(Collectors.toList())
                .block();
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForMyItems(Long requetorId,
                                                     BookingState state,
                                                     Integer from,
                                                     Integer size) {
        return client.get()
                .uri(serverUri + "/bookings/owner/"
                        + "?state=" + state
                        + "&from=" + from
                        + "&size=" + size)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToFlux(BookingDto.class)
                .collect(Collectors.toList())
                .block();
    }
}