package com.artspb.moviesearchapp.domain.impl

import com.artspb.moviesearchapp.domain.api.MoviesInteractor
import com.artspb.moviesearchapp.domain.api.MoviesRepository
import com.artspb.moviesearchapp.ui.inspector.ArchitectureFlowMonitor
import com.artspb.moviesearchapp.ui.inspector.LayerType
import java.util.concurrent.Executors

// Реализация интерактора в слое Domain.
// Interactor — это последний рубеж перед слоем представления. Именно здесь по правилам нашего курса
// мы создаем фоновый поток (используя Executor), чтобы синхронный запрос в репозитории не замораживал главный экран.
class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchMovies(expression: String, consumer: MoviesInteractor.MoviesConsumer) {
        ArchitectureFlowMonitor.logStep(
            layer = LayerType.DOMAIN,
            title = "MoviesInteractorImpl.searchMovies()",
            details = "Запуск выполнения задачи в фоновом потоке Executor и вызов MoviesRepository",
            payloadPreview = "expression = '$expression'"
        )

        executor.execute {
            // Выполняем поиск фильмов через репозиторий в фоновом потоке
            val movies = repository.searchMovies(expression)
            
            ArchitectureFlowMonitor.logStep(
                layer = LayerType.DOMAIN,
                title = "Результат в MoviesInteractorImpl",
                details = "Получен список фильмов от репозитория, вызов consumer.consume(movies)",
                payloadPreview = "foundMovies count = ${movies.size}"
            )

            // Передаем результат в коллбек UI-слоя
            consumer.consume(movies)
        }
    }
}
