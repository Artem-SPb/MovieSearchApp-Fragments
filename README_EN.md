<div align="center">
  <a href="README.md">🇷🇺 <b>Русский</b></a> | <a href="README_EN.md">🇬🇧 <b>English</b></a>
</div>

<br/>

<div align="center">
  <h1>🎬 MovieSearchApp — MVVM & Clean Architecture & Live Flow Inspector</h1>
  <p>
    <b>My educational project: refactoring a movie search app to the MVVM (Model-View-ViewModel) pattern on top of Clean Architecture, featuring a real-time visual representation of the layer flow right on the screen.</b>
  </p>

  <!-- Badges -->
  <img src="https://img.shields.io/badge/Kotlin-2.0.0-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Android%20SDK-API%2034-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Android SDK"/>
  <img src="https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean-blue?style=flat-square" alt="MVVM + Clean Architecture"/>
  <img src="https://img.shields.io/badge/Network-Retrofit%20%2B%20Gson-orange?style=flat-square" alt="Retrofit"/>
  <img src="https://img.shields.io/badge/Async-Executor%20%2F%20LiveData-yellow?style=flat-square" alt="LiveData & Executor"/>
</div>

---

## 📱 Project Preview

<div align="center">
  <img src="screenshots/preview.jpg" alt="MovieSearchApp MVVM Preview" width="680"/>
  <br/>
  <i>* An interactive panel displays in real-time how the request travels through the UI ➔ ViewModel ➔ Domain ➔ Data layers</i>
</div>

---

## 💡 About the Project

This project was originally built using Clean Architecture (with callbacks). As part of a new learning phase, I performed a deep refactoring and implemented the modern **MVVM (Model-View-ViewModel)** pattern.

Now, the `Activity` (UI layer) is completely decoupled from invoking interactors and business logic directly. Instead, it merely observes the data flow (via `LiveData`) provided by the `ViewModel`. Furthermore, I implemented an automatic search feature (**debounce** pattern) during text input, eliminating the need for a separate "Search" button.

For portfolio purposes, I also upgraded the built-in **Live Flow Tracker** (the "Wow-effect"): now the main screen live-tracks not only the path from Domain to Data but also the involvement of the `[🧠 ViewModel]` layer.

---

## 🔥 Refactoring Highlights

1. **Migration to MVVM Pattern**:
   - Created `MoviesViewModel` and `PosterViewModel`.
   - Introduced a state wrapper `MoviesState` (Loading, Content, Error, Empty).
   - The UI layer (`MoviesActivity`) now solely renders the state received via `LiveData` subscriptions.
2. **Debounce Search (Automatic Search)**:
   - The "Search" button was removed. A `TextWatcher` was implemented, utilizing a delay (debounce) to send a request only if the user has stopped typing for 2 seconds.
3. **Interactive HUD Architecture Inspector Update**:
   - Added a new `[🧠 ViewModel]` layer indicator with its signature (pink) color.
   - During a search, the indicators light up as the signal passes through: `UI ➔ ViewModel ➔ Domain ➔ Data ➔ Mapping ➔ ViewModel ➔ UI`.
   - The log bottom sheet (`BottomSheetDialog`) now clearly shows how the `ViewModel` manages screen states and debounces requests.
4. **Clean Architecture (Preserved)**:
   - The **Domain** layer remains written in pure Kotlin with zero platform dependencies.
   - The **Data** layer uses Retrofit and synchronous network requests (`execute()`), as concurrency is fully managed by the Domain layer (via `Executor`).

---

## 🚀 How to Run the Project

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Artem-SPb/MovieSearchApp-MVVM.git
   cd MovieSearchApp-MVVM
   ```
2. **Get a free OMDb API Key**:
   - Sign up at [omdbapi.com/apikey.aspx](https://www.omdbapi.com/apikey.aspx) and get a free key.
3. **Add the key to `local.properties`**:
   - Open or create a `local.properties` file in the project root.
   - Add your key in the following format:
     ```properties
     OMDB_API_KEY=YOUR_API_KEY
     ```
4. **Run the project in Android Studio**:
   - Build and run the application on an emulator or a real device (Android 8.0+ / API 26+).
   - Simply start typing a movie title in English (e.g., `Inception`, `Matrix`), and the automatic search will trigger after 2 seconds!
   - Open the **Layer Log** to explore the architectural data flow under the hood.

---

## 👨‍💻 Author

**Artem (Artem-SPb)**
- GitHub: [@Artem-SPb](https://github.com/Artem-SPb)

---

<div align="center">
  <p>⭐ If you liked this project and the MVVM implementation — I would highly appreciate your star on GitHub!</p>
</div>
