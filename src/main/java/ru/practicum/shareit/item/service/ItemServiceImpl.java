package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {


    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, CommentRepository commentRepository, BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Optional<ItemDto> create(Long ownerId, ItemDto itemDto) {
        log.info("Create Item OwnerID:{}; Item:{}", ownerId, itemDto);
        Item item = ItemMapper.toItem(itemDto, ownerId);
        if (item.getName() == null || item.getName().isBlank() ||
                item.getDescription() == null || item.getDescription().isBlank() ||
                item.getAvailable() == null ||
                item.getOwnerId() == null
        )
            throw new ValidationException("Ошибка валидации");

        userRepository.findById(item.getOwnerId()).orElseThrow(() -> new NotFoundException(item.getOwnerId().toString()));
        return Optional.ofNullable(ItemMapper.toDTO(itemRepository.save(item)));
    }

    @Override
    @Transactional
    public Optional<ItemDto> update(Long ownerId, Long itemId, ItemDto itemDto) {
        log.info("Update Item OwnerID:{}; ItemId:{}; Item:{}", ownerId, itemId, itemDto);

        Item item = ItemMapper.toItem(itemDto, ownerId, itemId);

        Item oldItem = itemRepository.findById(item.getId()).orElseThrow(ItemNotFoundException::new);

        if (!oldItem.getOwnerId().equals(item.getOwnerId())) {
            throw new NotFoundException("Не хозяин вещи");
        }

        if (item.getName() != null && !item.getName().isBlank())
            oldItem.setName(item.getName());

        if (item.getDescription() != null && !item.getDescription().isBlank())
            oldItem.setDescription(item.getDescription());

        if (item.getAvailable() != null)
            oldItem.setAvailable(item.getAvailable());

        return Optional.of(ItemMapper.toDTO(itemRepository.save(oldItem)));
    }

    @Override
    public Optional<ItemDto> get(Long id) {
        log.info("Get Item ItemId:{}", id);
        return Optional.of(ItemMapper.toDTO(itemRepository.findById(id).orElseThrow(ItemNotFoundException::new)));
    }

    @Override
    public Collection<ItemDto> search(String text) {
        log.info("Search Item filter:{}", text);
        if (text == null || text.isBlank())
            return new ArrayList<>();

        return itemRepository.searchItemsContainsTextAvailableTrue(text)
                .stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Collection<ItemDtoForOwner> getAll(Long ownerId) {
        log.info("GET all item ownerID:{}", ownerId);

        Collection<Item> itemList = itemRepository.findAllByOwnerId(ownerId);

        Collection<ItemDtoForOwner> itemDtoForOwners = itemList.stream()
                .map(x -> {
                    List<Booking> bookingList = bookingRepository.findAllByItem_IdOrderByStartDesc(x.getId());
                    return ItemMapper.itemDtoForOwnerFromItemAndBookingList(x, bookingList);
                })
                .collect(Collectors.toList());

        for (ItemDtoForOwner item : itemDtoForOwners) {
            item.setComments(commentRepository.findAllByItem_Id(item.getId()).stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList()));
        }

        return itemDtoForOwners;
    }

    @Override
    public Optional<ItemDtoForOwner> getItemWithOwnerCheck(Long itemId, Long requestorId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        List<Comment> comments = commentRepository.findAllByItem_Id(itemId);

        ItemDtoForOwner returnItemDto;
        if (Objects.equals(item.getOwnerId(), requestorId)) {
            returnItemDto = ItemMapper.itemDtoForOwnerFromItemAndBookingList(item,
                    bookingRepository.findAllByItem_IdOrderByStartDesc(itemId));
        } else {
            returnItemDto = ItemMapper.toItemDtoForOwner(item, null, null);
        }

        returnItemDto.setComments(comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));

        return Optional.ofNullable(returnItemDto);
    }

    @Override
    public Comment createComment(Comment comment, Long itemId, Long createrId) {
        comment.setItem(itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException())); //здесь произойдет проверка корректности itemId
        comment.setAuthor(userRepository.findById(createrId).orElseThrow(() -> new NotFoundException(createrId.toString()))); //здесь произойдет проверка корректности createrId
        comment.setCreated(LocalDateTime.now());

        Collection<Booking> bookings = bookingRepository.findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(createrId,
                LocalDateTime.now(),
                LocalDateTime.now());

        if (!bookings.stream()
                .map(x -> x.getItem().getId())
                .collect(Collectors.toList())
                .contains(itemId))
            throw new BadRequestException(" Только тот кто брал вещь в аренду может " +
                    "оставить отзыв и только после окончания аренды");


        return commentRepository.save(comment);
    }
}
