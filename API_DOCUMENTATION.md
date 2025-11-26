# API Documentation - Унифицированные справочники

## Обзор

API для управления унифицированными справочниками производственного холдинга. Включает управление предприятиями, подразделениями, материалами и оборудованием.

## Базовый URL

```
http://localhost:8080
```

## Swagger UI

Интерактивная документация API доступна по адресу:
```
http://localhost:8080/swagger-ui.html
```

---

## Endpoints

### 1. Enterprises (Предприятия)

#### Создать предприятие
```http
POST /api/enterprises
Content-Type: application/json

{
  "name": "Норильский ГМК",
  "type": "MINING",
  "location": "Норильск"
}
```

**Типы предприятий:** `MINING`, `PROCESSING`, `OFFICE`

**Ответ (201 Created):**
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

#### Получить все предприятия
```http
GET /api/enterprises
```

#### Получить предприятие по ID
```http
GET /api/enterprises/{id}
```

#### Обновить предприятие
```http
PUT /api/enterprises/{id}
Content-Type: application/json

{
  "name": "Новое название",
  "type": "PROCESSING",
  "location": "Санкт-Петербург"
}
```

#### Удалить предприятие
```http
DELETE /api/enterprises/{id}
```

---

### 2. Departments (Подразделения)

#### Создать подразделение
```http
POST /api/departments
Content-Type: application/json

{
  "enterpriseId": 1,
  "name": "Таймырский рудник",
  "type": "MINE"
}
```

**Типы подразделений:** `MINE`, `WORKSHOP`, `REPAIR_SERVICE`, `WAREHOUSE`, `FINANCE`, `HR`, `ADMINISTRATION`

#### Получить все подразделения
```http
GET /api/departments
```

#### Получить подразделения по ID предприятия
```http
GET /api/departments?enterpriseId=1
```

#### Получить подразделение по ID
```http
GET /api/departments/{id}
```

#### Обновить подразделение
```http
PUT /api/departments/{id}
Content-Type: application/json

{
  "name": "Новое название",
  "type": "WORKSHOP"
}
```

#### Удалить подразделение
```http
DELETE /api/departments/{id}
```

---

### 3. Materials (Материалы)

#### Создать материал
```http
POST /api/materials
Content-Type: application/json

{
  "name": "Медная руда",
  "category": "RAW_MATERIAL",
  "unit": "тонна",
  "description": "Сырая медная руда для переработки"
}
```

**Категории материалов:** `RAW_MATERIAL`, `SPARE_PART`, `REAGENT`, `FUEL`, `OTHER`

#### Получить все материалы
```http
GET /api/materials
```

#### Получить материал по ID
```http
GET /api/materials/{id}
```

#### Обновить материал
```http
PUT /api/materials/{id}
Content-Type: application/json

{
  "name": "Обновленное название",
  "category": "SPARE_PART",
  "unit": "шт"
}
```

#### Удалить материал
```http
DELETE /api/materials/{id}
```

---

### 4. Equipment (Оборудование)

#### Создать оборудование
```http
POST /api/equipment
Content-Type: application/json

{
  "departmentId": 1,
  "name": "Экскаватор Hitachi EX1200",
  "type": "EXCAVATOR",
  "inventoryNumber": "EXC-001-2023",
  "status": "OPERATIONAL"
}
```

**Типы оборудования:** `EXCAVATOR`, `TRUCK`, `FURNACE`, `REACTOR`, `CONVEYOR`, `OTHER`

**Статусы оборудования:** `OPERATIONAL`, `UNDER_REPAIR`, `IDLE`, `DECOMMISSIONED`

#### Получить все оборудование
```http
GET /api/equipment
```

#### Получить оборудование по ID подразделения
```http
GET /api/equipment?departmentId=1
```

#### Получить оборудование по статусу
```http
GET /api/equipment?status=OPERATIONAL
```

#### Получить оборудование по ID
```http
GET /api/equipment/{id}
```

#### Обновить оборудование
```http
PUT /api/equipment/{id}
Content-Type: application/json

{
  "name": "Обновленное название",
  "status": "UNDER_REPAIR"
}
```

#### Удалить оборудование
```http
DELETE /api/equipment/{id}
```

---

## Примеры curl запросов

### Создать предприятие
```bash
curl -X POST http://localhost:8080/api/enterprises \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Норильский ГМК",
    "type": "MINING",
    "location": "Норильск"
  }'
```

### Получить все предприятия
```bash
curl -X GET http://localhost:8080/api/enterprises
```

### Создать подразделение
```bash
curl -X POST http://localhost:8080/api/departments \
  -H "Content-Type: application/json" \
  -d '{
    "enterpriseId": 1,
    "name": "Таймырский рудник",
    "type": "MINE"
  }'
```

### Получить подразделения предприятия
```bash
curl -X GET "http://localhost:8080/api/departments?enterpriseId=1"
```

### Создать материал
```bash
curl -X POST http://localhost:8080/api/materials \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Медная руда",
    "category": "RAW_MATERIAL",
    "unit": "тонна",
    "description": "Сырая медная руда"
  }'
```

### Создать оборудование
```bash
curl -X POST http://localhost:8080/api/equipment \
  -H "Content-Type: application/json" \
  -d '{
    "departmentId": 1,
    "name": "Экскаватор Hitachi",
    "type": "EXCAVATOR",
    "inventoryNumber": "EXC-001-2023",
    "status": "OPERATIONAL"
  }'
```

### Получить оборудование по статусу
```bash
curl -X GET "http://localhost:8080/api/equipment?status=OPERATIONAL"
```

---

## Обработка ошибок

API возвращает стандартизированные ответы об ошибках:

### 404 Not Found
```json
{
  "status": 404,
  "message": "Enterprise not found with id: 999",
  "timestamp": "2025-10-05T12:00:00",
  "path": "/api/enterprises/999"
}
```

### 400 Bad Request (Валидация)
```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2025-10-05T12:00:00",
  "path": "/api/enterprises",
  "errors": {
    "name": "Name is required",
    "type": "Type is required"
  }
}
```

### 400 Bad Request (Бизнес-логика)
```json
{
  "status": 400,
  "message": "Enterprise not found with id: 999",
  "timestamp": "2025-10-05T12:00:00",
  "path": "/api/departments"
}
```

---

## Запуск приложения

### Требования
- Java 17
- PostgreSQL
- Redis
- Docker (опционально)

### С помощью Docker Compose
```bash
# Запустить PostgreSQL и Redis
docker-compose up -d

# Собрать приложение
./gradlew build

# Запустить приложение
./gradlew bootRun
```

### Вручную
```bash
# Убедиться что PostgreSQL и Redis запущены

# Создать базу данных
createdb holding_db

# Собрать и запустить
./gradlew clean build
./gradlew bootRun
```

Приложение будет доступно по адресу: `http://localhost:8080`

### Проверка работы
```bash
# Проверить здоровье приложения
curl http://localhost:8080/actuator/health

# Открыть Swagger UI
open http://localhost:8080/swagger-ui.html
```

---

## Тестовые данные

После запуска приложения в базе данных автоматически создаются тестовые данные:
- 6 предприятий (3 горнодобывающих, 2 перерабатывающих, 1 офисное)
- 23 подразделения
- 30 материалов
- 15 единиц оборудования

Вы можете сразу начать работу с API, используя существующие ID.

---

## Структура базы данных

### Таблицы
- `enterprises` - Предприятия
- `departments` - Подразделения (связь с enterprises)
- `materials` - Материалы
- `equipment` - Оборудование (связь с departments)

### Индексы
Созданы индексы на:
- Foreign keys (enterprise_id, department_id)
- Часто используемые поля (type, category, status)

---

## Технологический стек

- **Spring Boot 3.4.4** - Основной фреймворк
- **Spring Data JPA** - ORM
- **PostgreSQL** - База данных
- **Redis** - Хранение сессий
- **Flyway** - Миграции БД
- **Lombok** - Уменьшение boilerplate кода
- **SpringDoc OpenAPI** - Документация API (Swagger)
- **Spring Validation** - Валидация данных

---

## Дальнейшее развитие

Возможные улучшения:
- Добавить пагинацию для списков
- Добавить сортировку и фильтрацию
- Добавить поиск по названиям
- Добавить аудит изменений
- Добавить роли и права доступа
- Добавить unit и integration тесты
- Добавить кэширование
- Добавить метрики и мониторинг
