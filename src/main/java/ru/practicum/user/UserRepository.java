package ru.practicum.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    List<User> findByEmailContainingIgnoreCase(String emailSearch);

    List<UserShort> findAllByEmailContainingIgnoreCase(String emailSearch);

    boolean existsByIdGreaterThanEqual(@Nullable Long id);

    Page<User> getUsersById(Long id, Pageable pageable);
}