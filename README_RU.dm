# Multiuser Calorie-tracking Application 🚀

*Веб-приложение для подсчета калорий с анализом приемов пищи для поддержания здоровой диеты*

## 📌 Содержание
- [Основные функции](#-основные-функции)
- [Технологический стек](#-технологический-стек)
- [API Документация](#api-документация)
- [Схема базы данных](#-схема-базы-данных)
- [Быстрый старт](#-быстрый-старт)
- [Тестирование](#-тестирование)
- [Развертывание](#-развертывание)
- [Контакты](#-контакты)

## 🌟 Основные функции
### Работа с пользователями:
- ✅ Регистрация пользователей
- 🧮 Автоматический расчет дневного количества калорий, основывась на цели (Формула Харриса-Бенедикта)
  
### Отслеживание приемов пищи
- 🍽️ Создание блюд с макронутриентами
- 🕒 Добавления приема пищи с списком выбранных блюд

### Отчеты
- 📊 Ежедневный отчет (приемы пищи and количество калорий)
- ✅ Проверка, что не превысил дневную норму калорий
- 🗓️ История приемов пищи по дням

## 🛠 Технологический стек
| Категория       | Технологии                          |
|----------------|-----------------------------------|
| **Бэкенд**     | Java 17, Spring Boot 3, Web, Data JPA, Validation|
| **Базы данных**| PostgreSQL, Liquibase             |
| **Инфраструктура** | Docker, Docker Compose       |
| **Сборка**     | Maven|
| **Тестирование** | JUnit 5, Mockito, Testcontainers |

## API Документация

[![View in Postman](https://img.shields.io/badge/Postman-View_Documentation-FF6C37?logo=postman&logoColor=white)](https://documenter.getpostman.com/view/41252659/2sB2cUA2yf)

## 📊 Схема базы данных

```mermaid
erDiagram
    users ||--o{ meal : "has"
    meal ||--o{ meal_dish : "includes"
    dish ||--o{ meal_dish : "used in"

    users {
        bigint user_id PK
        varchar(255) name
        varchar(255) email
        int age
        float height_in_cm
        float weight_in_kg
        varchar(255) goal
        varchar(255) sex
    }

    meal {
        bigint meal_id PK
        bigint user_id FK
        varchar(255) name
        timestamp created_time
    }

    dish {
        bigint dish_id PK
        varchar(255) name
        int carbs
        int fats
        int proteins
        int calories_per_serving
    }

    meal_dish {
        bigint dish_id FK
        bigint meal_id FK
    }
```

## ⚡ Быстрый старт
1. Клонируйте репозиторий:
```bash
git clone https://github.com/LoucterSo/fitness-app
cd fitness-app
```
2. Запустите приложение:
```bash
docker-compose -f docker-compose-dev.yml up --build
```
3. Остановите приложение:
```bash
docker-compose -f docker-compose-dev.yml down -v
```

## 🧪 Тестирование
```bash
# Unit-tests
./mvnw test
```

## 🐳 Развертывание
### 1. Для локального запуска(без Docker):
Запусти приложение с 'local' профилем и измени необходимые настройки в application-local.yml
```properties
spring.profiles.active=local
```

### 2. В режиме разработки:
```bash
docker-compose -f docker-compose-dev.yml up
```
### 3. В режиме продакшена:
*Не забудь добавить .env файл с нужными значениями в корень проекта*
```bash
docker-compose -f docker-compose-prod.yml up
```

## 📧 Контакты
- Автор: Владислав Горелкин
- 📧 Email: vlad_gorelkin@inbox.ru | loucterso@gmail.com
- 💻 GitHub: [LoucterSo](https://github.com/LoucterSo)
