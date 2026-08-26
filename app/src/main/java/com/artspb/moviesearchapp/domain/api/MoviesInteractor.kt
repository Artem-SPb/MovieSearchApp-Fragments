package com.artspb.moviesearchapp.domain.api

import com.artspb.moviesearchapp.domain.models.Movie

// Интерфейс интерактора — это окно, через которое слой UI (Presentation) общается со слоем бизнес-логики (Domain).
// Так как сетевой запрос выполняется в отдельном потоке, для возврата списка найденных фильмов
// я использую Callback в виде вложенного интерфейса MoviesConsumer.
interface MoviesInteractor {
    fun searchMovies(expression: String, consumer: MoviesConsumer)

    interface MoviesConsumer {
        fun consume(foundMovies: List<Movie>)
    }
}
