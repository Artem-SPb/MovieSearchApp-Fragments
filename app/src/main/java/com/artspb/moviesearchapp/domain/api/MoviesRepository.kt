package com.artspb.moviesearchapp.domain.api

import com.artspb.moviesearchapp.domain.models.Movie
import com.artspb.moviesearchapp.domain.models.MovieDetails
import com.artspb.moviesearchapp.util.Resource

// Интерфейс репозитория описывает контракт для слоя Domain.
// Interactor использует этот интерфейс, чтобы получать список фильмов, и не подозревает о том,
// откуда именно берутся данные (из Retrofit, базы данных и т.д.).
interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
    fun getMovieDetails(movieId: String): Resource<MovieDetails>
}
