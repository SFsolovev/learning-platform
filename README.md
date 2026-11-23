# Learning Platform

Учебная платформа для онлайн-курсов: курсы, модули и уроки, записи студентов, задания и решения, тесты, отзывы и теги.
Итоговый проект курса «ORM-фреймворки для Java».

**Стек:** Java 17, Spring Boot 4, Spring Data JPA / Hibernate, PostgreSQL, Maven, Lombok, JUnit 5, H2 (тесты), Docker.

## Быстрый старт

### Вариант 1. Docker Compose (рекомендуется)

Нужен только Docker.

```bash
git clone https://github.com/SFsolovev/learning-platform.git
cd learning-platform
docker compose up --build
```

Приложение: http://localhost:8080. При первом запуске создаётся схема БД и загружаются демо-данные.

### Вариант 2. Локально

Нужны JDK 17+ и PostgreSQL.

1. Создайте базу: `CREATE DATABASE learning_platform;`
2. Запустите (при необходимости задайте переменные окружения, см. ниже):

```bash
./mvnw spring-boot:run        # Linux/macOS
.\mvnw.cmd spring-boot:run    # Windows
```

Базу данных можно поднять и через Docker: `docker compose up -d db`.

## Конфигурация

Параметры подключения не хранятся в коде, они задаются переменными окружения.

| Переменная | Значение по умолчанию (профиль `dev`) |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/learning_platform` |
| `DB_USER` | `postgres` |
| `DB_PASSWORD` | `postgres` |

Профили Spring:

- `dev` (по умолчанию): PostgreSQL, `ddl-auto: update`, загрузка демо-данных.
- `test`: H2 в памяти (режим PostgreSQL), `ddl-auto: create-drop`. Используется в тестах.

## Демо-данные

В профиле `dev` при пустой базе `DemoDataLoader` создаёт преподавателя (`teacher@demo.com`), двух студентов (`ivan@demo.com`, `maria@demo.com`), две категории, два курса с модулями и уроками, записи студентов на курсы и тест с вопросом. Если пользователи уже есть, загрузка пропускается.

Проверка: `GET http://localhost:8080/api/courses` и `GET http://localhost:8080/api/courses/1/structure`.

## REST API

Все ответы в JSON. Тела ответов это DTO, сущности наружу не отдаются.

| Метод | Путь | Описание |
|---|---|---|
| POST | `/api/users` | Создать пользователя `{name, email, role}` |
| GET | `/api/users`, `/api/users/{id}` | Список и пользователь по id |
| GET | `/api/users/{id}/enrollments` | Записи студента на курсы |
| GET | `/api/users/{id}/submissions` | Решения студента |
| GET | `/api/users/{id}/quiz-results` | Результаты тестов студента |
| POST, GET | `/api/categories` | Создать категорию `{name}` и список категорий |
| POST | `/api/courses` | Создать курс `{title, description, categoryId, teacherId}` |
| GET | `/api/courses`, `/api/courses/{id}` | Список курсов и курс по id |
| PUT | `/api/courses/{id}` | Изменить описание `{description}` |
| DELETE | `/api/courses/{id}` | Удалить курс |
| PUT | `/api/courses/{id}/teacher` | Сменить преподавателя `{teacherId}` |
| GET | `/api/courses/{id}/structure` | Курс с модулями и уроками |
| POST | `/api/courses/{id}/modules` | Добавить модуль `{title, orderIndex, description}` |
| POST, GET | `/api/modules/{id}/lessons` | Добавить урок `{title, content, videoUrl}` и список уроков |
| POST, GET | `/api/modules/{id}/quiz` | Создать тест `{title, timeLimit}` и получить тест модуля |
| POST, GET | `/api/lessons/{id}/assignments` | Создать задание `{title, description, dueDate, maxScore}` и список заданий |
| POST | `/api/courses/{id}/enrollments` | Записать студента `{studentId}` |
| GET | `/api/courses/{id}/enrollments` | Студенты курса |
| DELETE | `/api/courses/{id}/enrollments/{studentId}` | Отписать студента |
| POST | `/api/assignments/{id}/submit` | Отправить решение `{studentId, content}` |
| GET | `/api/assignments/{id}/submissions` | Решения по заданию |
| PUT | `/api/submissions/{id}/grade` | Оценить решение `{score, feedback}` |
| GET | `/api/quizzes/{id}` | Тест с вопросами (правильные ответы скрыты) |
| POST | `/api/quizzes/{id}/questions` | Добавить вопрос `{text, type, options:[{text, correct}]}` |
| POST | `/api/quizzes/{id}/take` | Пройти тест `{studentId, answers:{"questionId":[optionId]}}` |
| GET | `/api/quizzes/{id}/results` | Результаты теста |
| POST, GET | `/api/courses/{id}/reviews` | Оставить отзыв `{studentId, rating, comment}` и список отзывов |
| GET | `/api/courses/{id}/rating` | Средняя оценка и число отзывов |
| POST, GET | `/api/tags` | Создать тег `{name}` и список тегов |
| POST, DELETE | `/api/courses/{courseId}/tags/{tagId}` | Привязать или убрать тег |
| GET | `/api/tags/{tagId}/courses` | Курсы с тегом |

Примеры (PowerShell):

```powershell
Invoke-RestMethod -Uri http://localhost:8080/api/courses/1/structure | ConvertTo-Json -Depth 6
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/courses/1/enrollments -ContentType "application/json" -Body '{"studentId":3}'
```

### Ошибки

Единый формат `{timestamp, status, error, message}`, его формирует `GlobalExceptionHandler` (`@RestControllerAdvice`).

| Код | Когда |
|---|---|
| 400 | Ошибки валидации, некорректное тело запроса |
| 404 | Сущность не найдена |
| 409 | Нарушено бизнес-правило (повторная запись, повторное решение, дубликат email и т. п.) |
| 500 | Непредвиденная ошибка |

## Модель данных

15 сущностей (пакет `entity`): `User`, `Profile`, `Category`, `Tag`, `Course`, `CourseModule` (таблица `modules`), `Lesson`, `Assignment`, `Submission`, `Quiz`, `Question`, `AnswerOption`, `QuizSubmission`, `CourseReview`, `Enrollment`.

- 1-1: `User`–`Profile`, `CourseModule`–`Quiz`.
- 1-M: `User`(преподаватель)–`Course`, `Category`–`Course`, `Course`–`CourseModule`–`Lesson`–`Assignment`–`Submission`, `Quiz`–`Question`–`AnswerOption`, `Course`–`CourseReview`.
- M-M: `User`–`Course` через сущность `Enrollment` (с датой и статусом), `Course`–`Tag` через таблицу `course_tag`.

Ограничения уникальности: email пользователя, одна запись студента на курс, одно решение студента на задание, один отзыв студента на курс, один тест на модуль, имена тегов и категорий.

### Ленивая загрузка

Все связи `LAZY`. `spring.jpa.open-in-view=false`, поэтому вне транзакции обращение к ленивой коллекции даёт `LazyInitializationException` (показано тестом `LazyLoadingIT`). Решения в проекте: сервисные методы с `@Transactional` и маппинг в DTO внутри транзакции, `JOIN FETCH` (`findWithModulesById`), `@EntityGraph` (`findAll`, `findByTagsId`).

### Удаление и каскады

- Курс каскадно удаляет модули, уроки и задания; тест каскадно удаляет вопросы и варианты.
- Удалить курс, на который есть записи студентов, нельзя (`409`).
- Для M-M `Course`–`Tag` каскада нет: при удалении курса удаляются только строки `course_tag`, теги остаются.
- Решения (`Submission`) каскадом не удаляются.

## Архитектура

```
com.example.learning_platform
├── entity       JPA-сущности
├── repository   Spring Data JPA репозитории
├── service      бизнес-логика, транзакции
├── dto          record-ы запросов и ответов
├── web          REST-контроллеры, GlobalExceptionHandler
├── exception    NotFoundException, BusinessException
└── config       DemoDataLoader
```

Слои: контроллер → сервис → репозиторий. Зависимости внедряются через конструкторы (Lombok `@RequiredArgsConstructor`).

## Тесты

```bash
./mvnw test
```

Тесты идут на H2 (профиль `test`), Docker и PostgreSQL не нужны.

- Интеграционные (`*IT`, `@SpringBootTest`): курсы и каскады, записи, задания и решения, тесты, отзывы, теги, ленивая загрузка.
