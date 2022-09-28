package ru.practicum.shareit.booking.model;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;
    @NotNull
    @Column(name = "start_time")
    private LocalDateTime start; //дата и время начала бронирования
    @NotNull
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Booking booking = (Booking) o;
        return id != null && Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}