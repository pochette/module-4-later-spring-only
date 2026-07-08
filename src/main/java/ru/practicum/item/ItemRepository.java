package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.UserRepositoryCustom;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    List<Item> findByUserId(long userId);

    @Query("select i from Item i where i.userId = ?1 and i.url = ?2")
    Optional<Item> findByUserIdAndUrl(Long userId, String url);

    @Transactional
    @Modifying
    @Query("delete from Item i where i.userId = ?1 and i.id = ?2")
    void deleteByUserIdAndId(long userId, long itemId);
}