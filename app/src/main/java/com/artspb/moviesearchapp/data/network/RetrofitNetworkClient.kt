package com.artspb.moviesearchapp.data.network

import com.artspb.moviesearchapp.BuildConfig
import com.artspb.moviesearchapp.data.dto.MovieDetailsRequest
import com.artspb.moviesearchapp.data.dto.MoviesSearchRequest
import com.artspb.moviesearchapp.data.dto.Response
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitNetworkClient : NetworkClient {
    private val imdbBaseUrl = "https://www.omdbapi.com"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imdbService = retrofit.create(IMDbApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto !is MoviesSearchRequest && dto !is MovieDetailsRequest) {
            return Response().apply { resultCode = 400 }
        }

        return try {
            val resp = if (dto is MoviesSearchRequest) {
                imdbService.findMovie(BuildConfig.OMDB_API_KEY, dto.expression).execute()
            } else {
                imdbService.getMovieDetails(BuildConfig.OMDB_API_KEY, (dto as MovieDetailsRequest).movieId).execute()
            }

            val body = resp.body() ?: return Response().apply { resultCode = 500 }
            body.apply { resultCode = resp.code() }
        } catch (e: Exception) {
            Response().apply { resultCode = 500 }
        }
    }
}
