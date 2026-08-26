package com.artspb.moviesearchapp.data.dto

// Класс для передачи параметров поискового запроса в сетевой клиент на слое Data.
// По правилам Clean Architecture мы создаем отдельный DTO-класс под каждый запрос,
// даже если параметр всего один (строка с поисковым выражением expression).
data class MoviesSearchRequest(val expression: String)
