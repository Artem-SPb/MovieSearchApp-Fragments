package com.artspb.moviesearchapp.domain.models

// Чистая бизнес-модель фильма для слоя Domain.
// В отличие от MovieDto из слоя Data, здесь принципиально НЕТ аннотаций @SerializedName или Gson,
// чтобы наша основная логика приложения и UI вообще не зависели от того, как и откуда прилетает JSON с сервера.
data class Movie(
    val id: String,
    val resultType: String,
    val image: String,
    val title: String,
    val description: String
)
