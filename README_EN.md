# Movie Search App (Fragments) 🍿

[![ru](https://img.shields.io/badge/lang-ru-blue.svg)](https://github.com/Artem-SPb/MovieSearchApp-Fragments/blob/main/README.md)

A modern Android pet project demonstrating networking, the MVVM architectural pattern, Clean Architecture, and Fragments.

## 📱 Preview
![Movie Search App Preview](assets/images/preview.png)

## 🌟 Features
* **Movie Search**: Integration with the OMDb API to search for movies by title and fetch detailed information.
* **TabLayout & ViewPager2**: Smooth switching between "Poster" and "About" screens using swipes and tabs.
* **Dependency Injection**: Powered by **Koin** for efficient dependency management.
* **Custom Share Feature**: A Floating Action Button (FAB) allowing users to easily share the movie's title and IMDb rating with friends via an implicit Intent.
* **ViewBinding**: Safe and efficient access to UI components without `findViewById`.

## 🏗 Architecture & Tech Stack
* Kotlin
* MVVM (Model-View-ViewModel) + Clean Architecture (Data, Domain, Presentation)
* Retrofit 2 & GSON (Networking)
* Koin (Dependency Injection)
* ViewPager2 & TabLayout
* Glide (Image loading)
* ViewBinding

## 📝 About the Project
This is a self-guided assignment completed as part of an Android development course sprint. The main goal was to refactor an existing codebase: transitioning the movie details screen from a single Activity to multiple Fragments, setting up TabLayout with ViewPager2, adding a new network request to fetch full movie details, and properly organizing the code into Clean Architecture layers using Koin for DI.

## 👨‍💻 Author
**Artem-SPb** 
- GitHub: [@Artem-SPb](https://github.com/Artem-SPb)

---
*Created as an educational project to practice networking, architecture, and fragments in Android.*
