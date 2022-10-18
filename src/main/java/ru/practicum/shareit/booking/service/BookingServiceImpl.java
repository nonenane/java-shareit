package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }


    @Override
    public Optional<BookingDto> createBooking(Booking booking, Long itemId, Long bookerId) {
        if (booking.getId() != null) {
            throw new RuntimeException(" Неверное значение id.");
        }

        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new ValidationException("DateStart > DateEnd");
        }

        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        User booker = userRepository.findById(bookerId).orElseThrow(() -> new NotFoundException(bookerId.toString()));

        if (!item.getAvailable())
            throw new ItemNotAvailableException();

        if (Objects.equals(bookerId, item.getOwnerId()))
            throw new NotFoundException("Владелец не может бронировать собственную вещь.");

        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        return Optional.of(BookingMapper.toBookingDto(bookingRepository.save(booking)));
    }

    @Override
    public Optional<BookingDto> confirmBooking(Long bookingId, Boolean approved, Long requestorId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

        if (!Objects.equals(booking.getItem().getOwnerId(), requestorId))
            throw new AccessDeniedException();

        if (booking.getStatus() != BookingStatus.WAITING)
            throw new BadRequestException(" Бронирование не ожидает подтверждения.");

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return Optional.of(BookingMapper.toBookingDto(bookingRepository.save(booking)));
    }


    @Override
    public Optional<BookingDto> getBooking(Long bookingId, Long requestorId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

        if (!Objects.equals(booking.getBooker().getId(), requestorId)
                && !Objects.equals(booking.getItem().getOwnerId(), requestorId))
            throw new NotFoundException(" Только владелец вещи или создатель бронирования могут выполнить запрос.");

        return Optional.of(BookingMapper.toBookingDto(booking));
    }


    @Override
    public List<BookingDto> getAllMyBookings(Long bookerId, BookingState state, Integer from, Integer size) {
        userRepository.findById(bookerId).orElseThrow(() -> new NotFoundException(bookerId.toString()));

        switch (state) {
            case ALL:
                return bookingRepository.findAllByBooker_IdOrderByStartDesc(bookerId)
                        .stream()
                        .skip(from).limit(size)
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId,
                                LocalDateTime.now(),
                                LocalDateTime.now())
                        .stream()
                        .skip(from).limit(size)
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAllByBooker_IdAndStartBeforeAndEndBeforeOrderByStartDesc(bookerId,
                                LocalDateTime.now(),
                                LocalDateTime.now())
                        .stream()
                        .skip(from).limit(size)
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllByBooker_IdAndStartAfterAndEndAfterOrderByStartDesc(bookerId,
                                LocalDateTime.now(),
                                LocalDateTime.now())
                        .stream()
                        .skip(from).limit(size)
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllByStatusAndBooker_IdOrderByStartDesc(BookingStatus.WAITING,
                                bookerId)
                        .stream()
                        .skip(from).limit(size)
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findAllByStatusAndBooker_IdOrderByStartDesc(BookingStatus.REJECTED,
                                bookerId)
                        .stream()
                        .skip(from).limit(size)
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            default:
                throw new RuntimeException("Некорректный параметр state.");
        }
    }


    @Override
    public List<BookingDto> getAllBookingsForMyItems(Long ownerId, BookingState state, Integer from, Integer size) {
        userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException(ownerId.toString()));

        List<Long> idsList = bookingRepository.findUserItemsBookingsIds(ownerId);

        if (idsList.isEmpty())
            return new ArrayList<>();

        switch (state) {
            case ALL:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .skip(from).limit(size)
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStart().isBefore(LocalDateTime.now())
                                && x.getEnd().isAfter(LocalDateTime.now()))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .skip(from).limit(size)
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case PAST:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStart().isBefore(LocalDateTime.now())
                                && x.getEnd().isBefore(LocalDateTime.now()))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .skip(from).limit(size)
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStart().isAfter(LocalDateTime.now())
                                && x.getEnd().isAfter(LocalDateTime.now()))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .skip(from).limit(size)
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case WAITING:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStatus().equals(BookingStatus.WAITING))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .skip(from).limit(size)
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return idsList.stream()
                        .map(bookingRepository::findById)
                        .map(Optional::get)
                        .filter((x) -> x.getStatus().equals(BookingStatus.REJECTED))
                        .sorted((x, y) -> y.getStart().compareTo(x.getStart()))
                        .skip(from).limit(size)
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            default:
                throw new RuntimeException("Некорректный параметр state.");
        }
    }
}
