package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemClient {

    private final WebClient client;
    private final String serverUri;

    public ItemClient(@Value("${shareit-server.url}") String serverUri) {
        this.client = WebClient.create();
        this.serverUri = serverUri;
    }

    public Optional<ItemDto> create(Long requetorId, ItemDto itemDto) {
        return client.post()
                .uri(serverUri + "/items")
                .body(Mono.just(itemDto), ItemDto.class)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToMono(ItemDto.class)
                .blockOptional();
    }

    public Optional<ItemDto> update(Long requetorId, Long itemId, ItemDto itemDto) {
        return client.patch()
                .uri(serverUri + "/items/" + itemId)
                .body(Mono.just(itemDto), ItemDto.class)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToMono(ItemDto.class)
                .blockOptional();
    }

    public Optional<ItemDtoForOwner> getItem(Long itemId, Long requetorId) {

        return client.get()
                .uri(serverUri + "/items/" + itemId)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToMono(ItemDtoForOwner.class)
                .blockOptional();

    }

    public List<ItemDtoForOwner> getAll(Long requetorId, Integer from, Integer size) {
        return client.get()
                .uri(serverUri + "/items/"
                        + "?from=" + from
                        + "&size=" + size)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToFlux(ItemDtoForOwner.class)
                .collect(Collectors.toList())
                .block();
    }

    public List<ItemDto> search(Long requetorId, String text, Integer from, Integer size) {
        return client.get()
                .uri(serverUri + "/items/search/"
                        + "?text=" + text
                        + "&from=" + from
                        + "&size=" + size)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToFlux(ItemDto.class)
                .collect(Collectors.toList())
                .block();
    }

    public Optional<CommentDto> createComment(CommentDto commentDto, Long itemId, Long requetorId) {
        return client.post()
                .uri(serverUri + "/items/" + itemId + "/comment")
                .body(Mono.just(commentDto), CommentDto.class)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new NotFoundException(error))))
                .bodyToMono(CommentDto.class)
                .blockOptional();
    }
}