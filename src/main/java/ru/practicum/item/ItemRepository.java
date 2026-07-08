package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@RepositoryRestResource
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom, QuerydslPredicateExecutor<Item> {

    @Query(value = "select " +
            "from items as it left join users as us " +
            "on us.id = it.id " +
            "where (cast(us.registration_date as date)) between ?1 and ?2 " +
            "group by it.user_id", nativeQuery = true)

    List<ItemCountByUser> countByUserRegistered(LocalDate dateFrom, LocalDate dateTo);



    @Query("select new ru.practicum.item.ItemCountByUser(it.userId, count(it.id)) " +
            "from Item as it " +
            "where it.url like ?1 " +
            "group by it.userId " +
            "order by count(it.id) desc")
    List<ItemCountByUser> countItemsByUserId(String urlPart);

    @Transactional
    @Modifying
    @Query("delete from Item i where i.userId = ?1 and i.id = ?2")
    void deleteByUserIdAndId(long userId, long itemId);

    List<Item> findByUserId(long userId);

    @Query("select i from Item i where i.userId = ?1 and i.url = ?2")
    Optional<Item> findByUserIdAndUrl(Long userId, String url);

}