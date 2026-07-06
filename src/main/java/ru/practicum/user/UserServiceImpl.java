package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return UserDtoMapper.toDtosList(repository.findAll());
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = UserDtoMapper.toEntity(userDto);
        return UserDtoMapper.toDto(repository.save(user));
    }
}