# Протоколы и API взаимодействия компонент системы

## Содержание
1. [Архитектура системы](#архитектура-системы)
2. [Протоколы взаимодействия](#протоколы-взаимодействия)
3. [Внешние зависимости](#внешние-зависимости)
4. [API Endpoints](#api-endpoints)
5. [Модели данных](#модели-данных)
6. [Механизмы безопасности](#механизмы-безопасности)
7. [Обработка ошибок](#обработка-ошибок)
8. [Сценарии взаимодействия](#сценарии-взаимодействия)

---

## Архитектура системы

### Общая структура

```
┌─────────────────┐
│   HTTP Client   │
│  (Browser/App)  │
└────────┬────────┘
         │ HTTP/HTTPS
         │ REST API
         ▼
┌─────────────────────────────────────┐
│      Spring Boot Application        │
│  ┌───────────────────────────────┐  │
│  │   Controller Layer            │  │
│  │   - AuthController            │  │
│  │   - EnterpriseController      │  │
│  │   - DepartmentController      │  │
│  │   - MaterialController        │  │
│  │   - EquipmentController       │  │
│  └──────────┬────────────────────┘  │
│             │                        │
│  ┌──────────▼────────────────────┐  │
│  │   Service Layer               │  │
│  │   - AuthService               │  │
│  │   - EnterpriseService         │  │
│  │   - DepartmentService         │  │
│  │   - MaterialService           │  │
│  │   - EquipmentService          │  │
│  └──────────┬────────────────────┘  │
│             │                        │
│  ┌──────────▼────────────────────┐  │
│  │   Repository Layer (JPA)      │  │
│  └──────────┬────────────────────┘  │
│             │                        │
│  ┌──────────▼────────────────────┐  │
│  │   Security Layer              │  │
│  │   - SecurityConfig            │  │
│  │   - PasswordEncoder           │  │
│  │   - AuthEntryPoint            │  │
│  └───────────────────────────────┘  │
└─────────┬───────────────┬───────────┘
          │               │
          │ JDBC          │ Redis Protocol
          ▼               ▼
   ┌─────────────┐  ┌──────────────┐
   │ PostgreSQL  │  │    Redis     │
   │   Database  │  │ Session Store│
   └─────────────┘  └──────────────┘
```

### Технологический стек
- **Framework**: Spring Boot 3.4.4
- **Security**: Spring Security + Session Management
- **Database**: PostgreSQL (хранение данных)
- **Cache/Session**: Redis (хранение сессий)
- **ORM**: Spring Data JPA + Hibernate
- **Migration**: Flyway
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Validation**: Jakarta Validation

---

## Протоколы взаимодействия

### 1. HTTP REST API

**Базовый протокол**: HTTP/1.1
**Формат данных**: JSON
**Базовый URL**: `http://localhost:8080`

#### Заголовки запросов
```http
Content-Type: application/json
Cookie: JSESSIONID=<session-id>
```

#### Заголовки ответов
```http
Content-Type: application/json
Set-Cookie: JSESSIONID=<session-id>; Path=/; HttpOnly; SameSite=Strict
```

### 2. Аутентификация: Cookie-Based Session

**Механизм**: Session-based authentication с использованием HTTP Cookies
**Session Storage**: Redis
**Cookie Name**: `JSESSIONID`

#### Жизненный цикл сессии
1. **Регистрация/Вход** → Создание сессии в Redis → Установка Cookie
2. **Запрос с Cookie** → Проверка сессии в Redis → Валидация пользователя
3. **Выход** → Удаление сессии из Redis → Удаление Cookie

#### Параметры Cookie
```properties
Cookie Name: JSESSIONID
HTTP-Only: true
Secure: false (в продакшене должно быть true)
SameSite: Strict
Max-Age: 600 секунд (10 минут)
Domain: localhost
Path: /
```

#### Параметры Session
```properties
Timeout: 1 минута (для разработки)
Max Sessions per User: 100
Namespace в Redis: spring:session
Cleanup Cron: каждую минуту
```

### 3. CORS (Cross-Origin Resource Sharing)

Система поддерживает CORS для кросс-доменных запросов:
- Разрешены все origins (`*`)
- Поддерживаются методы: GET, POST, PUT, DELETE, OPTIONS
- Разрешена передача credentials (cookies)

---

## Внешние зависимости

### 1. PostgreSQL Database

**Протокол**: JDBC
**Драйвер**: `org.postgresql.Driver`
**Connection String**: `jdbc:postgresql://localhost:5432/holding_db`

#### Параметры подключения
```properties
Host: localhost
Port: 5432
Database: holding_db
Username: postgres
Password: postgres
```

#### Настройки JPA/Hibernate
```properties
Dialect: PostgreSQLDialect
DDL Auto: validate (не генерирует схему автоматически)
Format SQL: true (для отладки)
Open-in-view: false (для оптимизации производительности)
```

#### Структура таблиц
- `enterprises` - Предприятия
- `departments` - Подразделения (FK → enterprises)
- `materials` - Материалы
- `equipment` - Оборудование (FK → departments)
- `warehouses` - Склады (FK → enterprises)
- `material_inventory` - Остатки материалов (FK → warehouses, materials)
- `suppliers` - Поставщики
- `products` - Продукция
- `land_plots` - Земельные участки (FK → enterprises)

### 2. Redis Cache/Session Store

**Протокол**: Redis Protocol (RESP)
**Client**: Lettuce (асинхронный клиент)

#### Параметры подключения
```properties
Host: localhost
Port: 6379
Password: session
```

#### Использование
- **Session Storage**: Хранение HTTP сессий пользователей
- **Namespace**: `spring:session`
- **Indexed Sessions**: Поддержка поиска сессий по username

---

## API Endpoints

### 1. Authentication API (`/api/v1/auth`)

Управление аутентификацией и авторизацией пользователей.

#### 1.1 Регистрация пользователя

**Endpoint**: `POST /api/v1/auth/signup`
**Доступ**: Публичный (без авторизации)
**Content-Type**: `application/json`

**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response** (201 Created):
```json
{
  "id": 1,
  "email": "user@example.com",
  "roles": ["USER"]
}
```

**Headers Response**:
```http
Set-Cookie: JSESSIONID=abc123...; Path=/; HttpOnly; SameSite=Strict
```

#### 1.2 Вход в систему (Login)

**Endpoint**: `POST /api/v1/auth/signin`
**Доступ**: Публичный (без авторизации)
**Content-Type**: `application/json`

**Request Body**:
```json
{
  "email": "admin@admin.com",
  "password": "admin"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "email": "admin@admin.com",
  "roles": ["ADMIN", "USER"]
}
```

**Headers Response**:
```http
Set-Cookie: JSESSIONID=xyz789...; Path=/; HttpOnly; SameSite=Strict
```

**Процесс аутентификации**:
1. Клиент отправляет credentials (email + password)
2. Spring Security проверяет пользователя в БД
3. При успехе создается сессия в Redis
4. Устанавливается Cookie с JSESSIONID
5. Все последующие запросы должны содержать этот Cookie

#### 1.3 Проверка текущей сессии

**Endpoint**: `GET /api/v1/auth/check`
**Доступ**: Требуется аутентификация
**Cookie**: `JSESSIONID=<session-id>`

**Response** (200 OK):
```json
1
```
Возвращает ID текущего пользователя.

#### 1.4 Получить информацию о текущем пользователе

**Endpoint**: `GET /api/v1/auth/whoami`
**Доступ**: Требуется аутентификация
**Cookie**: `JSESSIONID=<session-id>`

**Response** (200 OK):
```json
[
  {
    "authority": "ADMIN"
  },
  {
    "authority": "USER"
  }
]
```

#### 1.5 Получить всех пользователей с ролями

**Endpoint**: `GET /api/v1/auth/users`
**Доступ**: Только для ADMIN
**Cookie**: `JSESSIONID=<session-id>`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "email": "admin@admin.com",
    "roles": ["ADMIN", "USER"]
  },
  {
    "id": 2,
    "email": "user@example.com",
    "roles": ["USER"]
  }
]
```

#### 1.6 Получить все доступные роли

**Endpoint**: `GET /api/v1/auth/roles`
**Доступ**: Только для ADMIN
**Cookie**: `JSESSIONID=<session-id>`

**Response** (200 OK):
```json
["ADMIN", "USER", "MANAGER"]
```

#### 1.7 Выход из системы (Logout)

**Endpoint**: `POST /api/v1/auth/logout`
**Доступ**: Требуется аутентификация
**Cookie**: `JSESSIONID=<session-id>`

**Response** (200 OK):
```
(Empty body)
```

**Headers Response**:
```http
Set-Cookie: JSESSIONID=; Max-Age=0; Path=/
```

**Процесс выхода**:
1. Удаление сессии из Redis
2. Очистка SecurityContext
3. Удаление Cookie на клиенте

---

### 2. Enterprises API (`/api/enterprises`)

Управление предприятиями холдинга.

#### 2.1 Создать предприятие

**Endpoint**: `POST /api/enterprises`
**Доступ**: Требуется аутентификация
**Cookie**: `JSESSIONID=<session-id>`

**Request Body**:
```json
{
  "name": "Норильский ГМК",
  "type": "MINING",
  "location": "Норильск"
}
```

**Доступные типы предприятий**:
- `MINING` - Горнодобывающее
- `PROCESSING` - Перерабатывающее
- `OFFICE` - Офисное

**Response** (201 Created):
```json
{
  "id": 1,
  "name": "Норильский ГМК",
  "type": "MINING",
  "location": "Норильск",
  "createdAt": "2025-10-05T12:00:00",
  "updatedAt": "2025-10-05T12:00:00"
}
```

#### 2.2 Получить все предприятия

**Endpoint**: `GET /api/enterprises`
**Доступ**: Требуется аутентификация
**Cookie**: `JSESSIONID=<session-id>`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "name": "Норильский ГМК",
    "type": "MINING",
    "location": "Норильск",
    "createdAt": "2025-10-05T12:00:00",
    "updatedAt": "2025-10-05T12:00:00"
  },
  {
    "id": 2,
    "name": "Медный завод",
    "type": "PROCESSING",
    "location": "Санкт-Петербург",
    "createdAt": "2025-10-05T13:00:00",
    "updatedAt": "2025-10-05T13:00:00"
  }
]
```

#### 2.3 Получить предприятие по ID

**Endpoint**: `GET /api/enterprises/{id}`
**Доступ**: Требуется аутентификация
**Cookie**: `JSESSIONID=<session-id>`

**Path Parameter**:
- `id` (Long) - ID предприятия

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "Норильский ГМК",
  "type": "MINING",
  "location": "Норильск",
  "createdAt": "2025-10-05T12:00:00",
  "updatedAt": "2025-10-05T12:00:00"
}
```

#### 2.4 Обновить предприятие

**Endpoint**: `PUT /api/enterprises/{id}`
**Доступ**: Требуется аутентификация
**Cookie**: `JSESSIONID=<session-id>`

**Path Parameter**:
- `id` (Long) - ID предприятия

**Request Body** (все поля опциональны):
```json
{
  "name": "Новое название",
  "type": "PROCESSING",
  "location": "Новая локация"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "Новое название",
  "type": "PROCESSING",
  "location": "Новая локация",
  "createdAt": "2025-10-05T12:00:00",
  "updatedAt": "2025-10-05T14:30:00"
}
```

#### 2.5 Удалить предприятие

**Endpoint**: `DELETE /api/enterprises/{id}`
**Доступ**: Требуется аутентификация
**Cookie**: `JSESSIONID=<session-id>`

**Path Parameter**:
- `id` (Long) - ID предприятия

**Response** (204 No Content):
```
(Empty body)
```

**Каскадное удаление**:
- При удалении предприятия удаляются все связанные подразделения
- При удалении подразделений удаляется все связанное оборудование

---

### 3. Departments API (`/api/departments`)

Управление подразделениями предприятий.

#### 3.1 Создать подразделение

**Endpoint**: `POST /api/departments`
**Доступ**: Требуется аутентификация
**Cookie**: `JSESSIONID=<session-id>`

**Request Body**:
```json
{
  "enterpriseId": 1,
  "name": "Таймырский рудник",
  "type": "MINE"
}
```

**Доступные типы подразделений**:
- `MINE` - Рудник
- `WORKSHOP` - Цех
- `REPAIR_SERVICE` - Ремонтная служба
- `WAREHOUSE` - Склад
- `FINANCE` - Финансовый отдел
- `HR` - Отдел кадров
- `ADMINISTRATION` - Администрация

**Response** (201 Created):
```json
{
  "id": 1,
  "enterpriseId": 1,
  "enterpriseName": "Норильский ГМК",
  "name": "Таймырский рудник",
  "type": "MINE",
  "createdAt": "2025-10-05T12:00:00",
  "updatedAt": "2025-10-05T12:00:00"
}
```

#### 3.2 Получить все подразделения

**Endpoint**: `GET /api/departments`
**Доступ**: Требуется аутентификация
**Cookie**: `JSESSIONID=<session-id>`

**Query Parameters** (опционально):
- `enterpriseId` (Long) - Фильтр по ID предприятия

**Примеры запросов**:
```http
GET /api/departments
GET /api/departments?enterpriseId=1
```

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "enterpriseId": 1,
    "enterpriseName": "Норильский ГМК",
    "name": "Таймырский рудник",
    "type": "MINE",
    "createdAt": "2025-10-05T12:00:00",
    "updatedAt": "2025-10-05T12:00:00"
  },
  {
    "id": 2,
    "enterpriseId": 1,
    "enterpriseName": "Норильский ГМК",
    "name": "Ремонтный цех",
    "type": "WORKSHOP",
    "createdAt": "2025-10-05T13:00:00",
    "updatedAt": "2025-10-05T13:00:00"
  }
]
```

#### 3.3 Получить подразделение по ID

**Endpoint**: `GET /api/departments/{id}`
**Доступ**: Требуется аутентификация

**Response** (200 OK):
```json
{
  "id": 1,
  "enterpriseId": 1,
  "enterpriseName": "Норильский ГМК",
  "name": "Таймырский рудник",
  "type": "MINE",
  "createdAt": "2025-10-05T12:00:00",
  "updatedAt": "2025-10-05T12:00:00"
}
```

#### 3.4 Обновить подразделение

**Endpoint**: `PUT /api/departments/{id}`
**Доступ**: Требуется аутентификация

**Request Body** (все поля опциональны):
```json
{
  "name": "Новое название",
  "type": "WORKSHOP"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "enterpriseId": 1,
  "enterpriseName": "Норильский ГМК",
  "name": "Новое название",
  "type": "WORKSHOP",
  "createdAt": "2025-10-05T12:00:00",
  "updatedAt": "2025-10-05T14:30:00"
}
```

#### 3.5 Удалить подразделение

**Endpoint**: `DELETE /api/departments/{id}`
**Доступ**: Требуется аутентификация

**Response** (204 No Content)

---

### 4. Materials API (`/api/materials`)

Управление материалами и сырьем.

#### 4.1 Создать материал

**Endpoint**: `POST /api/materials`
**Доступ**: Требуется аутентификация

**Request Body**:
```json
{
  "name": "Медная руда",
  "category": "RAW_MATERIAL",
  "unit": "тонна",
  "description": "Сырая медная руда для переработки"
}
```

**Доступные категории материалов**:
- `RAW_MATERIAL` - Сырье
- `SPARE_PART` - Запчасть
- `REAGENT` - Реагент
- `FUEL` - Топливо
- `OTHER` - Прочее

**Response** (201 Created):
```json
{
  "id": 1,
  "name": "Медная руда",
  "category": "RAW_MATERIAL",
  "unit": "тонна",
  "description": "Сырая медная руда для переработки",
  "createdAt": "2025-10-05T12:00:00",
  "updatedAt": "2025-10-05T12:00:00"
}
```

#### 4.2 Получить все материалы

**Endpoint**: `GET /api/materials`
**Доступ**: Требуется аутентификация

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "name": "Медная руда",
    "category": "RAW_MATERIAL",
    "unit": "тонна",
    "description": "Сырая медная руда для переработки",
    "createdAt": "2025-10-05T12:00:00",
    "updatedAt": "2025-10-05T12:00:00"
  },
  {
    "id": 2,
    "name": "Дизельное топливо",
    "category": "FUEL",
    "unit": "литр",
    "description": "ДТ для горной техники",
    "createdAt": "2025-10-05T13:00:00",
    "updatedAt": "2025-10-05T13:00:00"
  }
]
```

#### 4.3 Получить материал по ID

**Endpoint**: `GET /api/materials/{id}`
**Доступ**: Требуется аутентификация

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "Медная руда",
  "category": "RAW_MATERIAL",
  "unit": "тонна",
  "description": "Сырая медная руда для переработки",
  "createdAt": "2025-10-05T12:00:00",
  "updatedAt": "2025-10-05T12:00:00"
}
```

#### 4.4 Обновить материал

**Endpoint**: `PUT /api/materials/{id}`
**Доступ**: Требуется аутентификация

**Request Body** (все поля опциональны):
```json
{
  "name": "Обновленное название",
  "category": "SPARE_PART",
  "unit": "шт"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "Обновленное название",
  "category": "SPARE_PART",
  "unit": "шт",
  "description": "Сырая медная руда для переработки",
  "createdAt": "2025-10-05T12:00:00",
  "updatedAt": "2025-10-05T14:30:00"
}
```

#### 4.5 Удалить материал

**Endpoint**: `DELETE /api/materials/{id}`
**Доступ**: Требуется аутентификация

**Response** (204 No Content)

---

### 5. Equipment API (`/api/equipment`)

Управление оборудованием.

#### 5.1 Создать оборудование

**Endpoint**: `POST /api/equipment`
**Доступ**: Требуется аутентификация

**Request Body**:
```json
{
  "departmentId": 1,
  "name": "Экскаватор Hitachi EX1200",
  "type": "EXCAVATOR",
  "inventoryNumber": "EXC-001-2023",
  "status": "OPERATIONAL"
}
```

**Доступные типы оборудования**:
- `EXCAVATOR` - Экскаватор
- `TRUCK` - Грузовик
- `FURNACE` - Печь
- `REACTOR` - Реактор
- `CONVEYOR` - Конвейер
- `OTHER` - Прочее

**Доступные статусы оборудования**:
- `OPERATIONAL` - В работе
- `UNDER_REPAIR` - На ремонте
- `IDLE` - Простаивает
- `DECOMMISSIONED` - Списано

**Response** (201 Created):
```json
{
  "id": 1,
  "departmentId": 1,
  "departmentName": "Таймырский рудник",
  "name": "Экскаватор Hitachi EX1200",
  "type": "EXCAVATOR",
  "inventoryNumber": "EXC-001-2023",
  "status": "OPERATIONAL",
  "createdAt": "2025-10-05T12:00:00",
  "updatedAt": "2025-10-05T12:00:00"
}
```

#### 5.2 Получить все оборудование

**Endpoint**: `GET /api/equipment`
**Доступ**: Требуется аутентификация

**Query Parameters** (опционально):
- `departmentId` (Long) - Фильтр по ID подразделения
- `status` (EquipmentStatus) - Фильтр по статусу

**Примеры запросов**:
```http
GET /api/equipment
GET /api/equipment?departmentId=1
GET /api/equipment?status=OPERATIONAL
GET /api/equipment?status=UNDER_REPAIR
```

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "departmentId": 1,
    "departmentName": "Таймырский рудник",
    "name": "Экскаватор Hitachi EX1200",
    "type": "EXCAVATOR",
    "inventoryNumber": "EXC-001-2023",
    "status": "OPERATIONAL",
    "createdAt": "2025-10-05T12:00:00",
    "updatedAt": "2025-10-05T12:00:00"
  },
  {
    "id": 2,
    "departmentId": 1,
    "departmentName": "Таймырский рудник",
    "name": "БелАЗ 75131",
    "type": "TRUCK",
    "inventoryNumber": "TRK-002-2023",
    "status": "UNDER_REPAIR",
    "createdAt": "2025-10-05T13:00:00",
    "updatedAt": "2025-10-05T13:00:00"
  }
]
```

#### 5.3 Получить оборудование по ID

**Endpoint**: `GET /api/equipment/{id}`
**Доступ**: Требуется аутентификация

**Response** (200 OK):
```json
{
  "id": 1,
  "departmentId": 1,
  "departmentName": "Таймырский рудник",
  "name": "Экскаватор Hitachi EX1200",
  "type": "EXCAVATOR",
  "inventoryNumber": "EXC-001-2023",
  "status": "OPERATIONAL",
  "createdAt": "2025-10-05T12:00:00",
  "updatedAt": "2025-10-05T12:00:00"
}
```

#### 5.4 Обновить оборудование

**Endpoint**: `PUT /api/equipment/{id}`
**Доступ**: Требуется аутентификация

**Request Body** (все поля опциональны):
```json
{
  "name": "Обновленное название",
  "status": "UNDER_REPAIR"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "departmentId": 1,
  "departmentName": "Таймырский рудник",
  "name": "Обновленное название",
  "type": "EXCAVATOR",
  "inventoryNumber": "EXC-001-2023",
  "status": "UNDER_REPAIR",
  "createdAt": "2025-10-05T12:00:00",
  "updatedAt": "2025-10-05T14:30:00"
}
```

#### 5.5 Удалить оборудование

**Endpoint**: `DELETE /api/equipment/{id}`
**Доступ**: Требуется аутентификация

**Response** (204 No Content)

---

## Модели данных

### Форматы временных меток

Все временные метки используют формат ISO 8601:
```
2025-10-05T12:00:00
```

### Валидация данных

Система использует Jakarta Validation (JSR-380) для валидации:

**Enterprise (Предприятие)**:
```java
{
  "name": "required, не пустое",
  "type": "required, один из: MINING, PROCESSING, OFFICE",
  "location": "required, не пустое"
}
```

**Department (Подразделение)**:
```java
{
  "enterpriseId": "required, должно существовать",
  "name": "required, не пустое",
  "type": "required, один из: MINE, WORKSHOP, REPAIR_SERVICE, WAREHOUSE, FINANCE, HR, ADMINISTRATION"
}
```

**Material (Материал)**:
```java
{
  "name": "required, не пустое",
  "category": "required, один из: RAW_MATERIAL, SPARE_PART, REAGENT, FUEL, OTHER",
  "unit": "required, не пустое",
  "description": "optional"
}
```

**Equipment (Оборудование)**:
```java
{
  "departmentId": "required, должно существовать",
  "name": "required, не пустое",
  "type": "required, один из: EXCAVATOR, TRUCK, FURNACE, REACTOR, CONVEYOR, OTHER",
  "inventoryNumber": "required, уникальное",
  "status": "required, один из: OPERATIONAL, UNDER_REPAIR, IDLE, DECOMMISSIONED"
}
```

---

## Механизмы безопасности

### 1. Spring Security Configuration

**Уровни доступа**:

```java
Публичные endpoints (без аутентификации):
  - POST /api/v1/auth/signup
  - POST /api/v1/auth/signin
  - GET  /swagger-ui/**
  - GET  /v3/api-docs/**

Требуется аутентификация:
  - Все остальные /api/** endpoints

Требуется роль ADMIN:
  - GET  /api/v1/auth/users
  - GET  /api/v1/auth/roles
```

### 2. Session Management

**Redis Session Repository**:
```java
Namespace: spring:session
Storage: Redis Indexed Session Repository
Indexed by: username (principal)
Max Sessions per User: 100
Session Fixation Protection: newSession
```

**Session Attributes**:
```
spring:session:sessions:{sessionId}
  - creationTime
  - lastAccessedTime
  - maxInactiveInterval
  - sessionAttr:SPRING_SECURITY_CONTEXT
```

### 3. Password Encoding

**Algorithm**: BCrypt
**Strength**: 10 rounds (по умолчанию)

```java
Процесс регистрации:
1. Получение пароля в plaintext
2. Хеширование BCrypt
3. Сохранение хеша в БД
4. Plaintext password не сохраняется

Процесс входа:
1. Получение пароля в plaintext
2. Загрузка хеша из БД
3. Сравнение BCrypt.matches(plaintext, hash)
4. Создание сессии при успехе
```

### 4. CORS Configuration

```java
Allowed Origins: * (все домены)
Allowed Methods: GET, POST, PUT, DELETE, OPTIONS
Allowed Headers: *
Allow Credentials: true (передача cookies)
Max Age: 3600 секунд
```

### 5. CSRF Protection

**Статус**: DISABLED
**Причина**: Session-based authentication с cookies, REST API

**Примечание**: В продакшене рекомендуется включить CSRF protection для защиты от атак.

---

## Обработка ошибок

### Стандартный формат ошибок

Все ошибки возвращаются в едином формате:

```json
{
  "status": 404,
  "message": "Enterprise not found with id: 999",
  "timestamp": "2025-10-05T12:00:00",
  "path": "/api/enterprises/999"
}
```

### HTTP Status Codes

**2xx Success**:
- `200 OK` - Успешный запрос (GET, PUT)
- `201 Created` - Ресурс создан (POST)
- `204 No Content` - Успешное удаление (DELETE)

**4xx Client Errors**:
- `400 Bad Request` - Ошибка валидации или бизнес-логики
- `401 Unauthorized` - Не авторизован (нет сессии)
- `403 Forbidden` - Доступ запрещен (недостаточно прав)
- `404 Not Found` - Ресурс не найден

**5xx Server Errors**:
- `500 Internal Server Error` - Внутренняя ошибка сервера

### Примеры ошибок

#### 404 Not Found
```json
{
  "status": 404,
  "message": "Enterprise not found with id: 999",
  "timestamp": "2025-10-05T12:00:00",
  "path": "/api/enterprises/999"
}
```

#### 400 Bad Request (Валидация)
```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2025-10-05T12:00:00",
  "path": "/api/enterprises",
  "errors": {
    "name": "Name is required",
    "type": "Type is required",
    "location": "Location is required"
  }
}
```

#### 400 Bad Request (Бизнес-логика)
```json
{
  "status": 400,
  "message": "Enterprise not found with id: 999",
  "timestamp": "2025-10-05T12:00:00",
  "path": "/api/departments"
}
```

**Причина**: Попытка создать подразделение для несуществующего предприятия.

#### 400 Bad Request (Уникальность)
```json
{
  "status": 400,
  "message": "Equipment with inventory number 'EXC-001-2023' already exists",
  "timestamp": "2025-10-05T12:00:00",
  "path": "/api/equipment"
}
```

**Причина**: Попытка создать оборудование с дублирующимся инвентарным номером.

#### 401 Unauthorized
```json
{
  "status": 401,
  "message": "Full authentication is required to access this resource",
  "timestamp": "2025-10-05T12:00:00",
  "path": "/api/enterprises"
}
```

**Причина**: Запрос без Cookie или с невалидной сессией.

#### 403 Forbidden
```json
{
  "status": 403,
  "message": "Access Denied",
  "timestamp": "2025-10-05T12:00:00",
  "path": "/api/v1/auth/users"
}
```

**Причина**: Попытка доступа к endpoint, требующему роли ADMIN, без соответствующей роли.

---

## Сценарии взаимодействия

### Сценарий 1: Регистрация и первый запрос

```
Клиент                          Сервер                    Redis              PostgreSQL
  │                               │                          │                    │
  │ 1. POST /auth/signup          │                          │                    │
  ├──────────────────────────────>│                          │                    │
  │   {email, password}           │                          │                    │
  │                               │ 2. Хеширование пароля   │                    │
  │                               │                          │                    │
  │                               │ 3. INSERT user           │                    │
  │                               ├─────────────────────────────────────────────>│
  │                               │                          │                    │
  │                               │ 4. Создание сессии       │                    │
  │                               ├─────────────────────────>│                    │
  │                               │   SET spring:session:... │                    │
  │                               │                          │                    │
  │ 5. 201 Created                │                          │                    │
  │<──────────────────────────────┤                          │                    │
  │   Set-Cookie: JSESSIONID      │                          │                    │
  │                               │                          │                    │
  │ 6. GET /api/enterprises       │                          │                    │
  ├──────────────────────────────>│                          │                    │
  │   Cookie: JSESSIONID          │                          │                    │
  │                               │ 7. Проверка сессии       │                    │
  │                               ├─────────────────────────>│                    │
  │                               │   GET spring:session:... │                    │
  │                               │<─────────────────────────┤                    │
  │                               │   Session data           │                    │
  │                               │                          │                    │
  │                               │ 8. SELECT enterprises    │                    │
  │                               ├─────────────────────────────────────────────>│
  │                               │<─────────────────────────────────────────────┤
  │                               │   Data                   │                    │
  │ 9. 200 OK                     │                          │                    │
  │<──────────────────────────────┤                          │                    │
  │   [{enterprises}]             │                          │                    │
```

### Сценарий 2: Создание предприятия с подразделениями и оборудованием

```
1. Аутентификация:
   POST /api/v1/auth/signin
   → Получение JSESSIONID Cookie

2. Создание предприятия:
   POST /api/enterprises
   Body: {
     "name": "Новый ГОК",
     "type": "MINING",
     "location": "Красноярск"
   }
   → Response: { "id": 7, ... }

3. Создание подразделения:
   POST /api/departments
   Body: {
     "enterpriseId": 7,
     "name": "Карьер №1",
     "type": "MINE"
   }
   → Response: { "id": 24, ... }

4. Создание оборудования:
   POST /api/equipment
   Body: {
     "departmentId": 24,
     "name": "Экскаватор Komatsu",
     "type": "EXCAVATOR",
     "inventoryNumber": "EXC-NEW-001",
     "status": "OPERATIONAL"
   }
   → Response: { "id": 16, ... }

5. Проверка структуры:
   GET /api/departments?enterpriseId=7
   → Response: [{ "id": 24, "name": "Карьер №1", ... }]

   GET /api/equipment?departmentId=24
   → Response: [{ "id": 16, "name": "Экскаватор Komatsu", ... }]
```

### Сценарий 3: Фильтрация оборудования по статусу

```
1. Получить все оборудование на ремонте:
   GET /api/equipment?status=UNDER_REPAIR
   → Response: [{ "id": 2, "status": "UNDER_REPAIR", ... }]

2. Обновить статус оборудования:
   PUT /api/equipment/2
   Body: {
     "status": "OPERATIONAL"
   }
   → Response: { "id": 2, "status": "OPERATIONAL", ... }

3. Проверить изменение:
   GET /api/equipment?status=OPERATIONAL
   → Response: [{ "id": 1, ... }, { "id": 2, ... }, ...]
```

### Сценарий 4: Выход и повторный вход

```
Клиент                          Сервер                    Redis
  │                               │                          │
  │ 1. POST /auth/logout          │                          │
  ├──────────────────────────────>│                          │
  │   Cookie: JSESSIONID=abc123   │                          │
  │                               │ 2. DEL session           │
  │                               ├─────────────────────────>│
  │                               │                          │
  │                               │ 3. Clear SecurityContext │
  │                               │                          │
  │ 4. 200 OK                     │                          │
  │<──────────────────────────────┤                          │
  │   Set-Cookie: JSESSIONID=;    │                          │
  │   Max-Age=0                   │                          │
  │                               │                          │
  │ 5. GET /api/enterprises       │                          │
  ├──────────────────────────────>│                          │
  │   (no cookie)                 │                          │
  │                               │                          │
  │ 6. 401 Unauthorized           │                          │
  │<──────────────────────────────┤                          │
  │                               │                          │
  │ 7. POST /auth/signin          │                          │
  ├──────────────────────────────>│                          │
  │   {email, password}           │                          │
  │                               │ 8. Создание новой сессии │
  │                               ├─────────────────────────>│
  │                               │                          │
  │ 9. 200 OK                     │                          │
  │<──────────────────────────────┤                          │
  │   Set-Cookie: JSESSIONID=xyz  │                          │
```

### Сценарий 5: Каскадное удаление

```
1. Структура данных:
   Enterprise (id=1)
     └─ Department (id=1)
          ├─ Equipment (id=1)
          └─ Equipment (id=2)

2. Удаление предприятия:
   DELETE /api/enterprises/1
   → Response: 204 No Content

3. Автоматическое каскадное удаление:
   PostgreSQL:
     - DELETE FROM equipment WHERE department_id = 1
       (удалены equipment id=1, id=2)
     - DELETE FROM departments WHERE enterprise_id = 1
       (удален department id=1)
     - DELETE FROM enterprises WHERE id = 1
       (удалено enterprise id=1)

4. Проверка:
   GET /api/enterprises/1
   → Response: 404 Not Found

   GET /api/departments/1
   → Response: 404 Not Found

   GET /api/equipment/1
   → Response: 404 Not Found
```

---

## Производительность и оптимизация

### Индексы в PostgreSQL

Для оптимизации запросов созданы следующие индексы:

```sql
-- Foreign keys
CREATE INDEX idx_departments_enterprise_id ON departments(enterprise_id);
CREATE INDEX idx_equipment_department_id ON equipment(department_id);

-- Frequently queried columns
CREATE INDEX idx_enterprises_type ON enterprises(type);
CREATE INDEX idx_departments_type ON departments(type);
CREATE INDEX idx_materials_category ON materials(category);
CREATE INDEX idx_equipment_type ON equipment(type);
CREATE INDEX idx_equipment_status ON equipment(status);
```

### Кэширование

**Redis Session Cache**:
- Все HTTP сессии хранятся в Redis
- Быстрый доступ к сессиям (O(1))
- Автоматическая очистка истекших сессий

**JPA Second Level Cache**:
```properties
spring.jpa.properties.jakarta.persistence.sharedCache.mode=ALL
```
Включен кэш второго уровня для сущностей JPA.

### Connection Pooling

По умолчанию Spring Boot использует HikariCP для пула соединений с PostgreSQL:
- **Minimum Idle**: 10
- **Maximum Pool Size**: 10
- **Connection Timeout**: 30 секунд

---

## Мониторинг и логирование

### Логирование

```properties
logging.level.org.hibernate=ERROR
logging.level.org.springframework.jdbc=TRACE
logging.level.org.springframework.security=TRACE
```

**Что логируется**:
- SQL запросы (TRACE level)
- Spring Security события (TRACE level)
- Ошибки Hibernate (ERROR level)

### Health Check

Spring Boot Actuator (если включен):
```http
GET /actuator/health
```

---

## Swagger UI / OpenAPI

**URL**: `http://localhost:8080/swagger-ui.html`
**OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

Интерактивная документация API доступна через Swagger UI:
- Все endpoints с описаниями
- Возможность тестирования API
- Схемы моделей данных
- Примеры запросов и ответов

---

## Конфигурация окружения

### Development (application.properties)

```properties
# Server
server.port=8080

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/holding_db
spring.datasource.username=postgres
spring.datasource.password=postgres

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=session

# Session
spring.session.timeout=1m
server.servlet.session.timeout=1m
server.servlet.session.cookie.max-age=600

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# Admin
admin.email=admin@admin.com
```

### Production Recommendations

```properties
# Session (увеличить таймауты)
spring.session.timeout=30m
server.servlet.session.cookie.max-age=1800

# Cookie Security
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.same-site=strict

# CSRF Protection
# Включить CSRF в SecurityConfig

# CORS
# Ограничить allowed origins конкретными доменами

# Logging
logging.level.org.springframework.security=INFO
logging.level.org.springframework.jdbc=INFO

# Database
# Использовать пул соединений с оптимальными параметрами
```

---

## Заключение

Данный документ описывает все протоколы и API взаимодействия компонентов системы управления производственным холдингом. Система построена на базе современного Spring Boot stack с использованием:

- **REST API** для клиент-серверного взаимодействия
- **PostgreSQL** для хранения структурированных данных
- **Redis** для управления сессиями
- **Spring Security** для аутентификации и авторизации
- **Flyway** для версионирования схемы БД

Архитектура системы обеспечивает:
- Масштабируемость (stateless API + Redis sessions)
- Безопасность (аутентификация, авторизация, password hashing)
- Надежность (транзакции БД, каскадное удаление)
- Производительность (индексы, кэширование, connection pooling)
- Удобство разработки (Swagger UI, логирование, миграции)