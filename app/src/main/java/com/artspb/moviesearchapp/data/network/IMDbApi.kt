package com.artspb.moviesearchapp.data.network

import com.artspb.moviesearchapp.data.dto.MoviesSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Мой Retrofit-интерфейс для работы с OMDb API.
// Перенесен в пакет data.network, так как вся работа с сетью и сторонними библиотеками
// строго изолирована внутри слоя Data.
interface IMDbApi {
    @GET("/")
    fun findMovie(
        @Query("apikey") apiKey: String,
        @Query("s") expression: String
    ): Call<MoviesSearchResponse>
}
