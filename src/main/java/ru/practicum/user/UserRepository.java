package ru.practicum.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByIdGreaterThanEqual(@Nullable Long id);

    Page<User> getUsersById(Long id, Pageable pageable);
}