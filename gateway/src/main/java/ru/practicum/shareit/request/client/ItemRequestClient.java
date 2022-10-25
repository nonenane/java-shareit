package ru.practicum.shareit.request.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemRequestClient {

    private final WebClient client;
    private final String serverUri;

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUri) {
        this.client = WebClient.create();
        this.serverUri = serverUri;
    }

    public Optional<ItemRequestDto> createItemRequest(ItemRequestDto itemRequestDto, Long requetorId) {
        return client.post()
                .uri(serverUri + "/requests")
                .body(Mono.just(itemRequestDto), ItemRequestDto.class)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .bodyToMono(ItemRequestDto.class)
                .blockOptional();
    }

    public List<ItemRequestDto> getMyItemRequests(Long requetorId) {
        return client.get()
                .uri(serverUri + "/requests")
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .bodyToFlux(ItemRequestDto.class)
                .collect(Collectors.toList())
                .block();
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequests(Integer from, Integer size, Long requetorId) {

        return client.get()
                .uri(serverUri + "/requests/all/"
                        + "?from=" + from
                        + "&size=" + size)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .bodyToFlux(ItemRequestDto.class)
                .collect(Collectors.toList())
                .block();
    }

    @GetMapping("/{requestId}")
    public Optional<ItemRequestDto> getItemRequest(Long requestId, Long requetorId) {

        return client.get()
                .uri(serverUri + "/requests/" + requestId)
                .header("X-Sharer-User-Id", String.valueOf(requetorId))
                .retrieve()
                .bodyToMono(ItemRequestDto.class)
                .blockOptional();
    }
}
