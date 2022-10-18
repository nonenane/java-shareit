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

    @Query(value = "select * " +
            "from items as i " +
            "where i.name ilike CONCAT('%', ?1, '%') or i.description ilike CONCAT('%', ?1, '%') " +
            "and i.available = true " +
            "LIMIT ?3 OFFSET ?2", nativeQuery = true)
    Collection<Item> searchItemsPageContainsTextAvailableTrue(@NotNull String text,
                                                              @NotNull Integer from,
                                                              @NotNull Integer size);

    Collection<Item> findAllByOwnerId(Long ownerId);

    @Query(value = "select * " +
            "from items as i " +
            "where i.owner_id = ?1 " +
            "LIMIT ?3 OFFSET ?2", nativeQuery = true)
    Collection<Item> findPageByOwner_Id(@NotNull Long ownerId, @NotNull Integer from, @NotNull Integer size);

    Collection<Item> findAllByRequest_Id(Long requestId);
}
