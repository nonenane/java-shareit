package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items", catalog = "public")
public class Item {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "owner_id")
    private Long ownerId;
    @Column(name = "name")
    private String name;
    @Column(name = "description", length = 1024)
    private String description;
    @Column(name = "available", columnDefinition = "BooleanType", length = 1)
    private Boolean available;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
