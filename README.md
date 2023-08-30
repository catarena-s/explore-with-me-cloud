# Explore With Me Cloud

"Explore With Me Cloud" - это альтернативная реализация проекта ["Explore With Me"](https://github.com/catarena-s/java-explore-with-me) с использованием Spring Cloud. Это микросервисное приложение, предназначенное для планирования событий и создания групп для участия в различных мероприятиях.

## Структура проекта

Проект организован на основе микросервисов, каждый из которых выполняет 
определенные функции. 
Дополнительные модули, такие как `Eureka Server`, `Config Service` и `Gateway Service`, 
помогают обеспечить эффективное взаимодействие между микросервисами и управление настройками.

## Стек технологий

**Java 17**, **Spring**(Boot, Data, Cloud, Config), **Hibernate**(JPA), REST, JUnit, Mockito

Lombok, Liquibase, PostgreSQL, Maven, Docker, Postman

## TO DO
1. [ v ] Создать сервис Config Service для хранения и получения настроек с GitHub.
2. [ v ] Создать сервис EurekaServer - реестр сервисов для организации динамической регистрации и обнаружения микросервисов.
3. [ v ] Создать сервис Spring Gateway - маршрутизатор для перенаправления запросов к соответствующим микросервисам.
4. [ ] Перенести функционал из исходного проекта "Explore With Me".
    - [ ] Основной функционал  
    - [ ] Сервис статистики
5. [ ] Проверить работу и запуск тестов (при необходимости поправить).
6. [ ] Добавить Liquibase для управления миграциями базы данных.
7. [ ] Разделить основной функционал на микросервисы:
    - [ ] Сервис управления пользователями.
    - [ ] Сервис управления подборками мероприятий.
    - [ ] Сервис управления запросами на участие в мероприятии.
    - [ ] Сервис подписки на друзей.
    - [ ] Сервис управления мероприятиями и т.д.
8. [ ] Для каждого микросервиса создать отдельную базу данных.
9. [ ] Добавить работу с RabbitMQ или Kafka:
    - [ ] Реализовать оповещения о новых событиях.
    - [ ] Реализовать подтверждение участия и уведомления о новых участниках.




[//]: # (## Безопасность)

[//]: # ()
[//]: # (На данном этапе безопасность проекта ограничивается минимальными настройками. Дополнительные меры безопасности будут внедряться на более поздних этапах.)

[//]: # (## Сборка и развертывание)

[//]: # ()
[//]: # (- Сборка проекта осуществляется с помощью инструмента Maven.)

[//]: # (- Развертывание приложения предполагается в Docker-контейнерах.)

[//]: # ()
[//]: # (## Локальная разработка)

[//]: # ()
[//]: # (Для разработки на локальной машине выполните следующие шаги:)

[//]: # ()
[//]: # (1. Клонируйте репозиторий.)

[//]: # (2. Настройте настройки конфигурации из репозитория GitHub.)

[//]: # (3. Запустите необходимые микросервисы.)

[//]: # (4. Запустите основное приложение.)

[//]: # (5. Начните разработку!)

---

Исходный проект "Explore With Me" доступен [здесь](https://github.com/catarena-s/java-explore-with-me).

[//]: # (Создано с ❤️ в Explore With Me Cloud)
