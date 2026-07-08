package ru.practicum.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(path = "people")
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    @RestResource(path = "emails")
    List<User> findByEmailContainingIgnoreCase(@Param("email") String emailSearch);

    List<UserShort> findAllByEmailContainingIgnoreCase(String emailSearch);

    boolean existsByIdGreaterThanEqual(@Nullable Long id);

    Page<User> getUsersById(Long id, Pageable pageable);
}