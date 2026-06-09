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
| `chat-history-service` | 8083 | ✅ реализован |
| `connection-management` | 8090 | ✅ реализован (KV + оркестрация, NFR) |
| `messaging-server` | 8084, 8085, … | ✅ реализован (тонкий WS-слой, несколько инстансов) |
| `web-app-service` | 8088 | ✅ реализован (homepage + статичная страница теста WS) |
| `api-gateway` | 8080 | ✅ реализован (ЕДИНАЯ точка входа: маршрутизация + auth) |

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

## Запуск Chat History Service
```bash
mvn -pl chat-history-service spring-boot:run
```
- Internal (для Messaging Service): `POST /internal/messages` — сохранить сообщение, вернуть `messageId`, `seq`, `timestamp`
- Внешний API: `GET /api/chats/{chatId}/messages?limit=20&beforeSeq=` — курсорная история (новейшие сверху)
- H2-консоль: http://localhost:8083/h2-console (`jdbc:h2:file:./data/chats-db;AUTO_SERVER=TRUE`)
- Документ `chat_history` по слайду 78: `message_id`, `chat_type`+`chat_id` (= `group_id | channel_id`), `sender_id`, `timestamp`, `text`, плюс `seq`
- Compound-индекс `(chat_id, seq)` — аналог слайдов 144–146; курсор `beforeSeq` даёт пагинацию без N+1
- Примеры запросов — `chat-history-service/requests.http`

> `group_id | channel_id` со слайда сведены к паре `(chatType, chatId)` — единый ключ партиции
> (вариант из плана §4.3). `senderId` передаёт доверенный Messaging Service из auth-контекста.

## Запуск Connection Management (NFR-слой)
```bash
mvn -pl connection-management spring-boot:run
```
- Internal: `PUT/DELETE /internal/connections/{userId}` — реестр соединений `userId → serverUrl` (Key/Value);
  `GET /internal/connections` — снимок реестра (отладка)
- Internal: `POST /internal/messages/route` — оркестрация: resolve chatId → store в Chat History →
  получатели из Groups&Channels → fan-out онлайн-получателям прямым REST на их Messaging Server
- БД нет: реестр держится **in-memory** (`ConcurrentHashMap`) — заглушка Key/Value DB (в проде Redis)
- Зависит от: groups-channels (8082), chat-history (8083); presence в users (8081) — best-effort
- Примеры запросов — `connection-management/requests.http`

> Это «убранная из messaging service» логика (слайд 133). Connection Management — единственный новый
> **тип БД** (Key/Value) рядом с SQL (users) и NoSQL (chat-history); ради этого занятие 3 про «разные БД».
> Доставка между серверами — прямым REST; брокер/pub-sub помечен `// TODO: NFR` (слайды 132–134).

## Запуск Messaging Server (тонкий WS-слой, NFR) — несколько инстансов
```bash
# инстанс 1 (порт 8084):
mvn -pl messaging-server spring-boot:run
# инстанс 2 (порт 8085) — в отдельном терминале:
mvn -pl messaging-server spring-boot:run -Dspring-boot.run.arguments="--server.port=8085 --app.server-url=http://localhost:8085"
```
- WebSocket: `ws://localhost:8084/ws?token=<authToken>` (токен из users signup/login; валидируется на handshake)
- Конверт `{type, payload}`: клиент шлёт `SEND_DIRECT/SEND_GROUP/SEND_CHANNEL`; сервер шлёт `ACK`/`MESSAGE`/`ERROR`
- Internal: `POST /internal/deliver` — зовёт Connection Management для локальной доставки получателю
- Debug (без WS-клиента): `POST /api/messages/direct`, заголовок `X-User-Id`, тело `{toUserId, text}`
- БД нет — только локальная `Map<userId, session>` в памяти инстанса
- Зависит от: users (8081, валидация токена), connection-management (8090, регистрация + маршрутизация)
- Примеры — `messaging-server/requests.http`

> **Порядок старта для полного стека:** users (8081) → groups-channels (8082) → chat-history (8083)
> → connection-management (8090) → messaging-server ×N (8084, 8085). Логика доставки вынесена из
> messaging-server в Connection Management (слайд 133); доставка между серверами — прямым REST,
> брокер помечен `// TODO: NFR — pub/sub`.

## Запуск Web App Service (homepage + тест WS)
```bash
mvn -pl web-app-service spring-boot:run
```
- `GET /api/homepage` (заголовок `X-User-Id`, обычно проставляет шлюз) — чаты пользователя + превью сообщений
- Статичная страница теста WebSocket: открой в браузере http://localhost:8080/ (через шлюз) или напрямую http://localhost:8088/
- Зависит от: groups-channels (8082), chat-history (8083)

## Запуск API Gateway (единая точка входа)
```bash
mvn -pl api-gateway spring-boot:run
```
- **Всё ходит через 8080.** Маршруты: `/api/users/**`→users; `/api/users/{id}/chats` и `/api/groups|channels/**`→groups;
  `/api/chats/**`→chat-history; `/api/homepage` и статика→web-app; `/ws`→messaging-server
- Аутентификация: токен (`Authorization: Bearer <token>` или `?token=`) валидируется через Users,
  вниз пробрасывается `X-User-Id` (публичные пути: signup/login/статика). Нет/битый токен → 401
- Stack: Spring Cloud Gateway 5.0.0 (webflux), Spring Cloud BOM 2025.1.x под Spring Boot 4.0
- Примеры — `api-gateway/requests.http`

> **Порядок запуска полного стека:** users (8081) → groups-channels (8082) → chat-history (8083)
> → connection-management (8090) → messaging-server (8084[/8085]) → web-app-service (8088) → api-gateway (8080).
> Затем открой **http://localhost:8080/** — страница ручного теста WebSocket.

## Конвенции (из курса)
- WebSocket-конверт `{ "type": ..., "payload": ... }`.
- `senderId` берётся из auth-контекста, не из payload клиента.
- Lazy-создание личного чата 1-на-1 при первом сообщении.
- Курсорная пагинация истории без N+1.
