package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class UserServiceImpl implements UserService {
    private final UserRepository repository;


    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = repository.findAll();
        return UserMapper.toDtoList(users);
    }

    public void checkUsers() {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(0,
                32, sortById);
        do {
            // 3. Запрашиваем у репозитория очередную страницу с данными
            Page<User> page = repository.findAll(pageable);

            // 4. Получаем список пользователей на текущей странице и выполняем с ними какую-то логику
            // Например, просто выводим их в консоль
            page.getContent().forEach(user -> System.out.println("Проверяется пользователь: " + user));

            // 5. Если есть следующая страница, переключаемся на нее. Если нет, цикл завершится.
            pageable = page.nextPageable();
        } while (pageable.isPaged());
    }

    @Override
    @Transactional
    public UserDto saveUser(UserDto userDto) {
        User user = repository.save(UserMapper.toEntity(userDto));
        return UserMapper.toDto(user);
    }
}