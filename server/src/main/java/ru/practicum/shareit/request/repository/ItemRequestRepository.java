package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByPetitioner_IdOrderByCreatedDesc(Long requestorId);


    @Query(value = "select * " +
            "from requests as r " +
            "where r.petitioner_id <> ?1 " +
            "ORDER BY r.created DESC " +
            "LIMIT ?3 OFFSET ?2", nativeQuery = true)
    List<ItemRequest> findPageNotMyRequests(Long requestorId, Integer from, Integer size);

    @Query(value = "select * " +
            "from requests as r " +
            "where r.petitioner_id <> ?1 " +
            "ORDER BY r.created DESC", nativeQuery = true)
    List<ItemRequest> findAllNotMyRequests(Long requestorId);
}
