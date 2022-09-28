package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {


    @Query(value = "select * " +
            "from items as i " +
            "where i.name ilike CONCAT('%', ?1, '%') or i.description ilike CONCAT('%', ?1, '%')" +
            "and i.available = true", nativeQuery = true)
    Collection<Item> searchItemsContainsTextAvailableTrue(@NotNull String text);
    //Collection<Item> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    Collection<Item> findAllByOwnerId(Long ownerId);
}
