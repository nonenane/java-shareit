package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ItemMapper {

    public static Item toItem(ItemDto itemDto, Long ownerId) {
        return new Item(null, ownerId, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
    }

    public static Item toItem(ItemDto itemDto, Long ownerId, Long itemId) {
        return new Item(itemId, ownerId, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
    }

    public static ItemDto toDTO(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public static ItemDtoForOwner toItemDtoForOwner(Item item, Booking last, Booking next) {
        if (item == null)
            return null;

        return new ItemDtoForOwner(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                last == null ? null : new ItemDtoForOwner.Booking(last.getId(), last.getBooker().getId()),
                next == null ? null : new ItemDtoForOwner.Booking(next.getId(), next.getBooker().getId()),
                null);
    }

    public static ItemDtoForOwner itemDtoForOwnerFromItemAndBookingList(Item item, List<Booking> bookingList) {

        if (bookingList.isEmpty())
            return ItemMapper.toItemDtoForOwner(item, null, null);

        if (bookingList.size() == 1)
            return bookingList.get(0).getStart().isAfter(LocalDateTime.now()) ?
                    ItemMapper.toItemDtoForOwner(item, null, bookingList.get(0)) :
                    ItemMapper.toItemDtoForOwner(item, bookingList.get(0), null);

        Booking last = null;
        Booking next = null;

        for (Booking booking : bookingList) {
            if (booking.getStart().isBefore(LocalDateTime.now())) {
                last = booking;
                int index = bookingList.indexOf(booking);

                if (index != 0) {
                    next = bookingList.get(index - 1);
                }

                break;
            }
        }

        if (last == null)
            next = bookingList.get(bookingList.size() - 1);

        return ItemMapper.toItemDtoForOwner(item, last, next);
    }
}

