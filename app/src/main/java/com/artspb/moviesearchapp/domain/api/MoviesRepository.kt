package com.artspb.moviesearchapp.domain.api

import com.artspb.moviesearchapp.domain.models.Movie

// Интерфейс репозитория — наше "окно в мир данных" со стороны слоя Domain.
// Interactor использует этот интерфейс, чтобы получить список фильмов, и ему абсолютно всё равно,
// откуда именно эти данные берутся (из Retrofit, базы данных или кэша).
interface MoviesRepository {
    fun searchMovies(expression: String): List<Movie>
}
