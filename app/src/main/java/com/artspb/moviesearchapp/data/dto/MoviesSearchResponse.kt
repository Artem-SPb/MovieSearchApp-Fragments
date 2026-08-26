package com.artspb.moviesearchapp.data.dto

import com.google.gson.annotations.SerializedName

// DTO-ответ от сервера OMDb API. Наследуюсь от базового класса Response(),
// чтобы репозиторий мог проверить resultCode и получить список найденных фильмов (в формате MovieDto).
class MoviesSearchResponse(
    // В OMDb API массив с фильмами называется "Search", а не "results"
    @SerializedName("Search") val results: List<MovieDto>?,
    @SerializedName("Response") val response: String, // OMDb возвращает "True" или "False"
    @SerializedName("Error") val errorMessage: String?
) : Response()
