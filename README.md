# Later

> Сервис отложенного чтения: сохранение ссылок, автоматический разбор страниц, теги и заметки.

![Java](https://img.shields.io/badge/Java_21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.3-6DB33F?logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?logo=spring&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-0769AD)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker_Compose-2496ED?logo=docker&logoColor=white)

**Русский** · [English](README.en.md)

## О проекте

Бэкенд сервиса «прочитать позже», похожего на Pocket. Пользователь сохраняет ссылку, сервис сам открывает страницу и определяет итоговый URL после редиректов, тип контента, заголовок, а также наличие картинок и видео. Ссылки размечаются тегами и фильтруются, к ним можно добавлять заметки.

Проект — миграция легаси-приложения с «чистого» Spring Framework (сервлеты и Java-конфигурация) на Spring Boot. Каждый этап миграции сохранён в отдельной ветке, от `1_spring_and_servlets` до `7_business_logic_boot`.

**Ключевые решения:**

- **Разбор страниц.** HTTP-запрос и разбор HTML через jsoup вынесены в отдельный компонент `UrlMetadataRetriever` за интерфейсом, поэтому в тестах его легко заменить.
- **Гибкая выборка.** Ссылки фильтруются по статусу (прочитано или нет), типу контента (статья, картинка, видео) и тегам, сортируются по дате, заголовку или сайту. Динамические условия строятся через QueryDSL.
- **Проекции и кастомные репозитории.** Для облегчённых выборок используются интерфейсные проекции (`UserShort`), для нестандартных запросов — собственные реализации репозиториев.
- **Тесты на всех уровнях.** Юнит-тесты на Mockito, срезы `@WebMvcTest` и `@JsonTest`, интеграционные тесты с полным контекстом, а также MockWebServer для внешних HTTP-запросов.

## Стек

| Область | Технологии |
| --- | --- |
| Язык и фреймворк | Java 21, Spring Boot 3.3 (Web, Data JPA, Data REST) |
| Данные | PostgreSQL, Hibernate, QueryDSL, Hibernate JPA Metamodel |
| Интеграции | jsoup (разбор HTML) |
| Тесты | JUnit 5, Mockito, MockMvc, OkHttp MockWebServer |
| Сборка и запуск | Maven, Docker, Docker Compose |

## Архитектура

```text
src/main/java/ru/practicum
├── user/     # пользователи и их статусы
├── item/     # ссылки: сохранение, извлечение метаданных, теги, фильтры
├── note/     # заметки к ссылкам, поиск по URL и тегу
└── common/   # исключения и обработка ошибок
```

## Быстрый старт

Нужны Docker Desktop и свободный порт 8080.

```bash
docker compose up --build
```

Без Docker нужны JDK 21 и PostgreSQL:

```bash
mvn spring-boot:run
```

## API

Текущий пользователь передаётся в заголовке `X-Later-User-Id`.

| Ресурс | Эндпоинты |
| --- | --- |
| Пользователи | `GET /users`, `POST /users` |
| Ссылки | `POST /items`, `GET /items?state=&contentType=&sort=&limit=&tags=`, `PATCH /items`, `DELETE /items/{id}` |
| Заметки | `POST /notes`, `GET /notes`, `GET /notes?url=`, `GET /notes?tag=` |

Значения фильтров: `state` — `ALL`, `UNREAD`, `READ`; `contentType` — `ALL`, `ARTICLE`, `IMAGE`, `VIDEO`; `sort` — `NEWEST`, `OLDEST`, `TITLE`, `SITE`.

## Тестирование

```bash
mvn test
```

Интеграционные тесты ожидают PostgreSQL на `localhost:5432` с базой `test`; остальные тесты запускаются без внешних зависимостей.

**28 тестов** покрывают сервисы, контроллеры, репозитории и JSON-сериализацию DTO. Отдельно проверено извлечение метаданных: внешний сайт подменяется MockWebServer.

## Автор

**Андрей Бурдак** — [GitHub](https://github.com/pochette) · [Telegram @burdak_aa](https://t.me/burdak_aa) · [burdak.andrew@gmail.com](mailto:burdak.andrew@gmail.com)
