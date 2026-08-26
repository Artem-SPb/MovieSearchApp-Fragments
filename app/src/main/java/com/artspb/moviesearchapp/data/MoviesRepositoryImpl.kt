package com.artspb.moviesearchapp.data

import com.artspb.moviesearchapp.data.dto.MovieDetailsRequest
import com.artspb.moviesearchapp.data.dto.MovieDetailsResponse
import com.artspb.moviesearchapp.data.dto.MoviesSearchRequest
import com.artspb.moviesearchapp.data.dto.MoviesSearchResponse
import com.artspb.moviesearchapp.data.network.NetworkClient
import com.artspb.moviesearchapp.domain.api.MoviesRepository
import com.artspb.moviesearchapp.domain.models.Movie
import com.artspb.moviesearchapp.domain.models.MovieDetails
import com.artspb.moviesearchapp.util.Resource

class MoviesRepositoryImpl(private val networkClient: NetworkClient) : MoviesRepository {

    override fun searchMovies(expression: String): Resource<List<Movie>> {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        return when (response.resultCode) {
            -1, 500 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                with(response as MoviesSearchResponse) {
                    Resource.Success((results ?: emptyList()).map {
                        Movie(it.id, it.resultType, it.image, it.title, it.description)
                    })
                }
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun getMovieDetails(movieId: String): Resource<MovieDetails> {
        val response = networkClient.doRequest(MovieDetailsRequest(movieId))
        return when (response.resultCode) {
            -1, 500 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                with(response as MovieDetailsResponse) {
                    Resource.Success(
                        MovieDetails(
                            id = id ?: "", 
                            title = title ?: "",
                            imDbRating = imDbRating ?: "", 
                            year = year ?: "",
                            countries = countries ?: "",
                            genres = genres ?: "",
                            directors = directors ?: "",
                            writers = writers ?: "",
                            stars = stars ?: "",
                            plot = plot ?: ""
                        )
                    )
                }
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}
