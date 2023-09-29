# Explore With Me Cloud

"Explore With Me Cloud" - альтернативная реализация проекта ["Explore With Me"](https://github.com/catarena-s/java-explore-with-me) с использованием Spring Cloud. Это бэкенд микросервисного приложения, предназначенного для планирования событий и создания групп для участия в различных мероприятиях.

## Структура проекта

Проект организован на основе микросервисов, каждый из которых выполняет определенные функции и имеет свою базу данных. Дополнительные модули, такие как `Eureka Server`, `Config Service`, и `Gateway Service`, помогают обеспечить эффективное взаимодействие между микросервисами и управление настройками.

## Стек технологий
**Java 17**, **Spring Framework**(Boot, Data, Cloud, Security), **Hibernate**(JPA), REST, Lombok, Liquibase, PostgreSQL, RabbitMQ, OAuth2, Keycloak, Maven, Postman

## Модули

### Сервис управления пользователями(ewm-users)

Этот сервис отвечает за управление пользователями в системе. Он предоставляет API для регистрации новых пользователей, аутентификации, обновления профилей пользователей и многое другое. Важная часть этого сервиса - это управление данными пользователей, их авторизация и безопасность.

### Сервис управления подборками мероприятий(ewm-compilation)

Этот сервис занимается созданием и управлением подборками мероприятий. Подборки могут быть тематическими, событийными или какими-либо другими категориями мероприятий. Пользователи могут создавать и редактировать подборки, добавлять мероприятия в них и делиться ими с другими пользователями.

### Сервис управления запросами на участие в мероприятии(ewm-request)

Этот сервис обеспечивает управление запросами от пользователей, желающих участвовать в конкретных мероприятиях из подборок. Пользователи могут отправлять запросы на участие, а организаторы мероприятий могут принимать или отклонять эти запросы.

### Сервис подписки на друзей(ewm-subscription)

Этот сервис предоставляет возможность пользователям подписываться на других пользователей и получать уведомления о событиях и активностях, связанных с их друзьями. Это способствует социальной взаимосвязи в приложении и позволяет пользователям быть в курсе событий, которые интересны их друзьям.

### Сервис управления мероприятиями(ewm-event)

Этот сервис отвечает за создание, редактирование и управление мероприятиями. Пользователи могут создавать мероприятия различных типов, например, выставки, спортивные мероприятия, походы в кино и многое другое. Сервис также предоставляет информацию о доступных мероприятиях, и пользователи могут искать и присоединяться к ним.

Каждый из этих сервисов является отдельным микросервисом и имеет свою собственную базу данных для хранения данных. Это позволяет масштабировать и разрабатывать каждый сервис независимо, что делает приложение более гибким и эффективным.

## TO DO
1. [x] Создать сервис для получения настроек с GitHub.
2. [x] Создать сервис EurekaServer - реестр сервисов для организации динамической регистрации и обнаружения микросервисов.
3. [x] Создать сервис Spring Gateway - маршрутизатор для перенаправления запросов к соответствующим микросервисам.
4. [x] Перенести функционал из исходного проекта "Explore With Me".
5. [x] Проверить работу и запуск тестов (при необходимости поправить).
6. [x] Добавить Liquibase для управления миграциями базы данных.
7. [x] Разделить основной функционал на микросервисы:
   - [x] Сервис управления пользователями.
   - [x] Сервис управления подборками мероприятий.
   - [x] Сервис управления запросами на участие в мероприятии.
   - [x] Сервис подписки на друзей.
   - [x] Сервис управления мероприятиями и т.д.
8. [x] Для каждого микросервиса создать отдельную базу данных.
9. [ ] Добавить работу с RabbitMQ или Kafka:
   - [x] Реализовать оповещения о новых событиях.
   - [x] Реализовать подтверждение участия и уведомления о новых участниках.
   - [x] Реализовать уведомление о запросе на дружбу, подтверждение/отклонение запроса на дружбу.
10. [x] Внедрить Spring Security для обеспечения безопасности приложения.
11. [ ] Настройка мониторинга:
    1. [ ] настроить сбор метрик приложения с использованием Prometheus
    2. [ ] настроить визуализацию данных с помощью Grafana для мониторинга производительности и состояния приложения.
12. [ ] Внедрение паттернов отказоустойчивости:
    - [ ] _Circuit Breaker_: Применение паттерна "Circuit Breaker" для обработки сбоев и автоматического перехода к режиму отказа при возникновении серийных сбоев во взаимодействии с другими сервисами.
    - [ ] _Bulkhead_: Реализация паттерна "Bulkhead" для изоляции частей системы и предотвращения распространения сбоев на другие компоненты.
    - [ ] _Rate Limiter_: Внедрение паттерна "Rate Limiter" для контроля над частотой обращения к ресурсам и предотвращения перегрузки системы.
    - [ ] _Retry: Создание_ механизма "Retry" для повторных попыток выполнения операций при временных сбоях.

## Использование RabbitMQ
Для обеспечения асинхронной обработки событий и оповещений в приложении внедрено RabbitMQ:

- **Оповещения о новых событиях**: Когда пользователь создает новое событие, информация о событии отправляется в RabbitMQ.
  Это позволяет заинтересованным пользователям быстро узнавать о новых событиях и реагировать на них.

- **Подтверждение участия и уведомления о новых участниках**: Если пользователь решает присоединиться к событию, его решение
  также отправляется в RabbitMQ. Организаторы события или другие участники могут получать уведомления о новых участниках.

- **Запросы на дружбу и уведомление о подтверждении/отклонении запроса**:Запросы на установление дружеских связей и
  уведомления о подтверждении или отклонении этих запросов также осуществляются через RabbitMQ. При создании запроса на
  установление дружбы информация о нем передается через RabbitMQ, а решение пользователя о его подтверждении или отклонении
  также обрабатывается с использованием RabbitMQ.

## Аутентификация и безопасность

Для обеспечения безопасности в бэкенд-части приложения "Explore With Me Cloud", использовались следующие средства:

- **Spring Security и JWT-аутентификация**: Spring Security гарантирует безопасность приложения, позволяя только аутентифицированным пользователям получать доступ к защищенным ресурсам. Для аутентификации используется JWT (JSON Web Token), который обеспечивает безопасное подтверждение идентификации пользователя.

- **JWT-авторизация**: Spring Security позволяет управлять авторизацией пользователей на основе их ролей и прав, что делает систему более гибкой. JWT-токены содержат информацию о ролях пользователя, и это используется для определения доступных действий и ресурсов.

- **Интеграция OAuth2 и Keycloak**: Для дополнительного уровня безопасности интегрированы OAuth2 и Keycloak. Keycloak предоставляет удобное управление идентификацией и авторизацией пользователей, а Spring Security и JWT-токены обогащают эту систему безопасности.

- **Защита ресурсов**: Spring Security и JWT-токенов обеспечивают защиту различных ресурсов и API, гарантируя доступ только аутентифицированным и авторизованным пользователям.

---

Исходный проект "Explore With Me" доступен [здесь](https://github.com/catarena-s/java-explore-with-me).
