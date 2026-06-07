# Messaging Platform — микросервисы (System Design course)

Учебная реализация архитектуры из занятия 2. Подробный план:
`../2026.06.03 - Lesson 2/План реализации микросервисов.md`.

## Требования
- **JDK 25** (LTS)
- **Maven 3.9+**

## Сервисы и порты
| Модуль | Порт | Статус |
|---|---|---|
| `users-service` | 8081 | ✅ реализован |
| `groups-channels-service` | 8082 | ✅ реализован |
| `chat-history-service` | 8083 | ⏳ план |
| `messaging-service` | 8084 | ⏳ план |
| `web-app-service` | 8080 | ⏳ опционально |

## Сборка
```bash
mvn clean install            # все модули
mvn -pl users-service compile
```

## Запуск Users Service
```bash
mvn -pl users-service spring-boot:run
```
- API: http://localhost:8081/api/users
- H2-консоль: http://localhost:8081/h2-console
  (JDBC URL: `jdbc:h2:file:./data/users-db;AUTO_SERVER=TRUE`, user `sa`, без пароля)
- Данные на диске: `users-service/data/users-db.mv.db` (переживают рестарт)

Примеры запросов — `users-service/requests.http`.

## Запуск Groups & Channels Service
```bash
mvn -pl groups-channels-service spring-boot:run
```
- API: http://localhost:8082/api/channels , http://localhost:8082/api/users/{id}/chats
- Internal (для Messaging Service): `/internal/groups/resolve`, `/internal/groups/{id}/members`, `/internal/channels/{id}/subscribers`
- H2-консоль: http://localhost:8082/h2-console (`jdbc:h2:file:./data/groups-db;AUTO_SERVER=TRUE`)
- Таблицы как на слайдах 71–75: `groups`, `group_participants`, `channels`, `channel_subscribers`
- Личность вызывающего — заголовок `X-User-Id` (имитация auth-контекста за шлюзом)
- Примеры запросов — `groups-channels-service/requests.http`

> Группа/приватный чат создаётся лениво через `/internal/groups/resolve` (нет публичного createGroup,
> как и в API на слайде 29). Приватный чат 1-на-1 = группа из двух участников.

## Конвенции (из курса)
- WebSocket-конверт `{ "type": ..., "payload": ... }`.
- `senderId` берётся из auth-контекста, не из payload клиента.
- Lazy-создание личного чата 1-на-1 при первом сообщении.
- Курсорная пагинация истории без N+1.
