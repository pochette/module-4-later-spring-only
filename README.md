# Later Backend (Spring Framework → Spring Boot)

Приложение, которое достанется вам, предназначено для сохранения и систематизации полезных веб-ссылок. Его основная идея — дать пользователю возможность собирать подборки интересных материалов и иметь к ним доступ с любых устройств.
Представьте: вы дизайнер, ежедневно читаете множество статей, смотрите кейсы и исследования. Это приложение становится вашим личным хранилищем лучших публикаций. При этом вы можете использовать его как для себя, так и делиться подборками с коллегами или единомышленниками.

> Цель обучения: перенос легаси-проекта с чистом Spring Framework на Spring Boot, а так же изучение основных инструментов современной разработки.
> Разработка ведется по отдельным веткам. 
> Учебный backend-проект, демонстрирующий полный цикл разработки современного Java-приложения: от классического Spring Framework до Spring Boot с покрытием тестами, контейнеризацией и автоматизацией сборки.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring](https://img.shields.io/badge/Spring_Framework-6.x-success)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue)
![Docker](https://img.shields.io/badge/Docker-enabled-2496ED)
![JUnit5](https://img.shields.io/badge/JUnit-5-green)
![Mockito](https://img.shields.io/badge/Mockito-5-lightgrey)
![GitHub Actions](https://img.shields.io/badge/CI/CD-GitHub_Actions-blue)

---

# О проекте

Проект выполнен в рамках курса **«Java-разработчик» Яндекс Практикума**.

Главной целью являлось изучение полного жизненного цикла backend-разработки:

* разработка REST API;
* работа с Spring Framework;
* перенос приложения на Spring Boot;
* проектирование базы данных;
* использование Spring Data JPA и Hibernate;
* написание модульных, интеграционных и web-тестов;
* контейнеризация приложения;
* автоматизация сборки и тестирования с помощью CI/CD.

Проект постепенно развивался по мере изучения новых технологий, что отражено в истории коммитов и отдельных ветках репозитория.

---

# Реализованный функционал

### Пользователи

* создание пользователей;
* обновление информации;
* получение пользователя;
* получение списка пользователей.

### Вещи

* добавление вещей;
* редактирование;
* поиск вещей;
* получение списка вещей владельца.

### Бронирования

* создание бронирования;
* подтверждение владельцем;
* отклонение бронирования;
* получение истории бронирований;
* фильтрация по состояниям.

### Комментарии

* публикация комментариев после завершения аренды;
* получение комментариев вместе с информацией о вещи.

### Запросы вещей

* создание запросов;
* просмотр собственных запросов;
* просмотр запросов других пользователей.

---

# Технологический стек

## Язык

* Java 21

## Framework

* Spring Framework
* Spring Boot

## Работа с данными

* Spring Data JPA
* Hibernate
* PostgreSQL

## Сборка

* Maven

## Контейнеризация

* Docker
* Docker Compose

## Тестирование

* JUnit 5
* Mockito
* Spring Boot Test
* MockMvc
* DataJpaTest
* WebMvcTest

## Инструменты

* Git
* GitHub
* IntelliJ IDEA

## CI/CD

* GitHub Actions
* автоматическая сборка проекта;
* запуск тестов при Push и Pull Request;
* проверка успешности сборки.

---

# Архитектура

Проект реализован по многослойной архитектуре.

```text
Client
   │
REST API
   │
Controller
   │
Service
   │
Repository
   │
PostgreSQL
```

Основные пакеты:

```text
controller
dto
mapper
service
repository
model
exception
validation
configuration
```

---

# Основные изученные темы

Во время разработки проекта были изучены и применены на практике:

### Java

* ООП;
* Collections Framework;
* Stream API;
* Optional;
* Generics;
* исключения;
* многопоточность (базовые принципы).

### Spring

* Dependency Injection;
* IoC Container;
* Java Configuration;
* Bean Lifecycle;
* REST Controller;
* Validation;
* Exception Handling.

### Spring Boot

* автоконфигурация;
* профили приложения;
* свойства конфигурации;
* логирование;
* работа с окружениями.

### Persistence

* JPA;
* Hibernate;
* связи между сущностями;
* ленивая и жадная загрузка;
* транзакции;
* SQL;
* PostgreSQL.

### Тестирование

Реализованы различные уровни тестирования:

* Unit-тесты;
* Integration-тесты;
* Repository-тесты;
* Controller-тесты;
* MockMvc-тестирование REST API;
* тестирование слоя Service;
* использование Mockito для изоляции зависимостей.

### Docker

* контейнеризация приложения;
* запуск PostgreSQL в Docker;
* Docker Compose;
* работа с образами и контейнерами.

### CI/CD

Настроен процесс непрерывной интеграции:

* автоматическая сборка Maven;
* запуск тестов;
* проверка Pull Request;
* использование GitHub Actions.

---

# Запуск

## Клонирование

```bash
git clone https://github.com/pochette/module-4-later-spring-only.git
```

## Сборка

```bash
mvn clean verify
```

## Запуск приложения

```bash
mvn spring-boot:run
```

или

```bash
docker compose up --build
```

---

# Полученные навыки

В рамках проекта были получены практические навыки:

* разработка REST API;
* проектирование backend-приложений;
* миграция приложения со Spring Framework на Spring Boot;
* использование Spring Data JPA;
* работа с Hibernate;
* проектирование базы данных;
* написание тестов разных уровней;
* применение Docker в разработке;
* настройка CI/CD;
* использование Git Flow;
* работа с Maven;
* обработка ошибок и валидация данных.

---

# Статус проекта

Проект завершён и представляет собой итоговую демонстрацию освоения современных технологий Java Backend-разработки в рамках курса **«Java-разработчик» Яндекс Практикума**.

---

# Автор

**Андрей**

Студент 3 курса бакалавриата по направлению **«Прикладная информатика»** Академия ИМСИТ, г. Краснодар

Java Backend Developer

GitHub: https://github.com/pochette
