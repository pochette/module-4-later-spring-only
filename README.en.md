# Later

> Read-it-later service: saving links, automatic page parsing, tags and notes.

![Java](https://img.shields.io/badge/Java_21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.3-6DB33F?logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?logo=spring&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-0769AD)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker_Compose-2496ED?logo=docker&logoColor=white)

[Русский](README.md) · **English**

## About

The backend of a Pocket-like "read it later" service. A user saves a link; the service fetches the page and resolves the final URL after redirects, the content type, the title, and whether it has images or video. Links are tagged and filtered, and notes can be attached to them.

The project is a migration of a legacy application from plain Spring Framework (servlets and Java configuration) to Spring Boot. Each migration stage is kept in its own branch, from `1_spring_and_servlets` to `7_business_logic_boot`.

**Key decisions:**

- **Page parsing.** The HTTP request and HTML parsing with jsoup are isolated in a `UrlMetadataRetriever` component behind an interface, so it is easy to replace in tests.
- **Flexible querying.** Links are filtered by status (read / unread), content type (article, image, video) and tags, and sorted by date, title or site. Dynamic conditions are built with QueryDSL.
- **Projections and custom repositories.** Interface projections (`UserShort`) serve lightweight reads; custom repository implementations handle non-standard queries.
- **Tests at every level.** Mockito unit tests, `@WebMvcTest` and `@JsonTest` slices, full-context integration tests, and MockWebServer for outgoing HTTP calls.

## Tech stack

| Area | Technologies |
| --- | --- |
| Language and framework | Java 21, Spring Boot 3.3 (Web, Data JPA, Data REST) |
| Data | PostgreSQL, Hibernate, QueryDSL, Hibernate JPA Metamodel |
| Integrations | jsoup (HTML parsing) |
| Testing | JUnit 5, Mockito, MockMvc, OkHttp MockWebServer |
| Build and run | Maven, Docker, Docker Compose |

## Architecture

```text
src/main/java/ru/practicum
├── user/     # users and their states
├── item/     # links: saving, metadata extraction, tags, filters
├── note/     # notes on links, search by URL and tag
└── common/   # exceptions and error handling
```

## Quick start

Requires Docker Desktop and a free port 8080.

```bash
docker compose up --build
```

Without Docker you need JDK 21 and PostgreSQL:

```bash
mvn spring-boot:run
```

## API

The current user is passed in the `X-Later-User-Id` header.

| Resource | Endpoints |
| --- | --- |
| Users | `GET /users`, `POST /users` |
| Links | `POST /items`, `GET /items?state=&contentType=&sort=&limit=&tags=`, `PATCH /items`, `DELETE /items/{id}` |
| Notes | `POST /notes`, `GET /notes`, `GET /notes?url=`, `GET /notes?tag=` |

Filter values: `state` — `ALL`, `UNREAD`, `READ`; `contentType` — `ALL`, `ARTICLE`, `IMAGE`, `VIDEO`; `sort` — `NEWEST`, `OLDEST`, `TITLE`, `SITE`.

## Testing

```bash
mvn test
```

Integration tests expect PostgreSQL on `localhost:5432` with a `test` database; the remaining tests run without external dependencies.

**28 tests** cover services, controllers, repositories and DTO JSON serialization. Metadata extraction is tested separately, with the external site replaced by MockWebServer.

## Author

**Andrew Burdak** — [GitHub](https://github.com/pochette) · [Telegram @burdak_aa](https://t.me/burdak_aa) · [burdak.andrew@gmail.com](mailto:burdak.andrew@gmail.com)
