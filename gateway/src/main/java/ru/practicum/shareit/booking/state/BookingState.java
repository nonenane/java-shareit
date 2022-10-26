package ru.practicum.shareit.booking.state;

/**
 * По умолчанию равен ALL (англ. «все»).
 * CURRENT (англ. «текущие»), PAST (англ. «завершённые»),
 * FUTURE (англ. «будущие»), WAITING (англ. «ожидающие подтверждения»), REJECTED (англ. «отклонённые»).
 */
public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED
}
