package com.artspb.moviesearchapp.data.dto

import com.google.gson.annotations.SerializedName

// DTO (Data Transfer Object) — сетевая модель фильма для работы на слое Data.
// Здесь я использую аннотации @SerializedName, чтобы Gson автоматически парсил поля из JSON OMDb API.
// После того как данные покинут слой Data, они будут преобразованы в чистую модель Movie для слоя Domain,
// чтобы бизнес-логика не зависела от сетевых библиотек.
data class MovieDto(
    @SerializedName("imdbID") val id: String,
    @SerializedName("Type") val resultType: String,
    @SerializedName("Poster") val image: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val description: String // Год выпуска запишем в поле description
)
