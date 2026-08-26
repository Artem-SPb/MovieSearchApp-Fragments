package com.artspb.moviesearchapp.data.network

import com.artspb.moviesearchapp.data.dto.Response

// Абстрактный интерфейс сетевого клиента в слое Data.
// Репозиторию не важно, какая именно библиотека (Retrofit, Ktor или OkHttp) выполняет запрос.
// Он общается с сетью через этот простой интерфейс, принимая любой DTO и возвращая базовый Response.
interface NetworkClient {
    fun doRequest(dto: Any): Response
}
