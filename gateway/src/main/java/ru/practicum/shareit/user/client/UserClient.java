package ru.practicum.shareit.user.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserClient {
    private final WebClient client;
    private final String serverUri;


    public UserClient(@Value("${shareit-server.url}") String serverUri) {
        this.client = WebClient.create();
        this.serverUri = serverUri;
    }

    public Optional<UserDTO> get(Long userId) {

        return client.get()
                .uri(serverUri + "/users/" + userId)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToMono(UserDTO.class)
                .blockOptional();
    }

    public List<UserDTO> getAll() {
        return client.get()
                .uri(serverUri + "/users")
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToFlux(UserDTO.class)
                .collect(Collectors.toList())
                .block();
    }

    public Optional<UserDTO> create(UserDTO userDto) {
        return client.post()
                .uri(serverUri + "/users")
                .body(Mono.just(userDto), UserDTO.class)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToMono(UserDTO.class)
                .blockOptional();
    }

    public Optional<UserDTO> update(Long requestorId, UserDTO userDto) {
        return client.patch()
                .uri(serverUri + "/users/" + requestorId)
                .body(Mono.just(userDto), UserDTO.class)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToMono(UserDTO.class)
                .blockOptional();
    }

    public void delete(Long userId) {
        client.delete()
                .uri(serverUri + "/users/" + userId)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToMono(Void.class)
                .block();
    }

}
