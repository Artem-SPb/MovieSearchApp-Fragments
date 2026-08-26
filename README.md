# Movie Search App (Fragments) 🍿

[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/Artem-SPb/MovieSearchApp-Fragments/blob/main/README_EN.md)

Современный Android пет-проект, демонстрирующий работу с сетью, архитектурным паттерном MVVM, Clean Architecture и фрагментами. 

## 📱 Превью
![Превью проекта Movie Search App](assets/images/preview.png)

## 🌟 Особенности
* **Поиск фильмов**: Интеграция с OMDb API для поиска фильмов по названию и получения детальной информации.
* **TabLayout & ViewPager2**: Плавное переключение между экранами "Постер" и "О фильме" с помощью свайпов и вкладок.
* **Dependency Injection**: Использование библиотеки **Koin** для внедрения зависимостей.
* **Кастомные фичи**: Плавающая кнопка (Floating Action Button) для быстрой отправки рейтинга и названия фильма друзьям через неявный Intent (Share).
* **ViewBinding**: Безопасный и удобный доступ к компонентам UI.

## 🏗 Архитектура и технологии
* Kotlin
* MVVM (Model-View-ViewModel) + Clean Architecture (Data, Domain, Presentation)
* Retrofit 2 & GSON (Сетевые запросы)
* Koin (Dependency Injection)
* ViewPager2 & TabLayout
* Glide (Загрузка изображений)
* ViewBinding

## 📝 О проекте
Это самостоятельная работа в рамках спринта по изучению Android-разработки. Главной задачей было выполнить рефакторинг уже существующего кода: перенести отображение деталей фильма из Activity во Фрагменты, настроить работу TabLayout с ViewPager2, добавить новый сетевой запрос для получения информации о фильме и правильно разделить логику по слоям (Clean Architecture) с использованием Koin для DI. 

## 👨‍💻 Автор
**Artem-SPb** 
- GitHub: [@Artem-SPb](https://github.com/Artem-SPb)

---
*Создано в качестве учебного проекта для практики работы с сетью, архитектурой и фрагментами в Android.*
