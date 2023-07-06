package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> findAllByOwnerId(Long userId);

    @Query("SELECT i FROM Item AS i " +
            "WHERE (LOWER(i.name) LIKE LOWER(concat('%', ?1, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(concat('%', ?1, '%'))) AND i.available=true")
    Collection<Item> findByNameOrDescriptionAndAvailable(String text);
}
