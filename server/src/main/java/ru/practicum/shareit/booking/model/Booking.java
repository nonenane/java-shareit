package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;
    @Column(name = "start_time")
    private LocalDateTime start; //дата и время начала бронирования
    @Column(name = "end_time")
    private LocalDateTime end; //дата и время конца бронирования
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item; //вещь, которую пользователь бронирует
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker; //пользователь, который осуществляет бронирование
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}