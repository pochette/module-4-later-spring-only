package ru.practicum.user;

import java.util.List;

public interface UserRepositoryCustom {
    List<UserShortWithIp> findAllByEmailContainingIgnoreCaseWithIP(String emailSearch);
}
