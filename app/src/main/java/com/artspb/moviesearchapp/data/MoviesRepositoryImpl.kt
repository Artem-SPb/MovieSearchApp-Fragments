package com.artspb.moviesearchapp.data

import com.artspb.moviesearchapp.data.dto.MoviesSearchRequest
import com.artspb.moviesearchapp.data.dto.MoviesSearchResponse
import com.artspb.moviesearchapp.data.network.NetworkClient
import com.artspb.moviesearchapp.domain.api.MoviesRepository
import com.artspb.moviesearchapp.domain.models.Movie
import com.artspb.moviesearchapp.ui.inspector.ArchitectureFlowMonitor
import com.artspb.moviesearchapp.ui.inspector.LayerType

// Реализация интерфейса MoviesRepository.
// Согласно лекции по Clean Architecture, этот класс должен находиться именно в слое Data (или в пакете data),
// потому что здесь мы напрямую работаем с DTO-классами (MoviesSearchRequest, MoviesSearchResponse).
// После получения ответа от NetworkClient мы маппим (преобразуем) сетевые DTO в чистые Domain-модели Movie.
class MoviesRepositoryImpl(private val networkClient: NetworkClient) : MoviesRepository {

    override fun searchMovies(expression: String): List<Movie> {
        // Обращаюсь к сетевому клиенту через интерфейс, передавая DTO запроса.
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        if (response.resultCode == 200 && response is MoviesSearchResponse) {
            val dtoResponse = response
            
            ArchitectureFlowMonitor.logStep(
                layer = LayerType.MAPPING,
                title = "Маппинг в MoviesRepositoryImpl",
                details = "Преобразование сетевых DTO моделей в чистые бизнес-модели Domain (Movie)",
                payloadPreview = "Mapping: ${dtoResponse.results?.size ?: 0} MovieDto ➔ List<Movie>"
            )

            // В OMDb API если фильмы найдены, то response == "True" и results != null.
            // Преобразую MovieDto в чистые бизнес-модели Movie с помощью map {}.
            if (dtoResponse.response == "True" && dtoResponse.results != null) {
                return dtoResponse.results.map {
                    Movie(it.id, it.resultType, it.image, it.title, it.description)
                }
            } else {
                return emptyList()
            }
        } else {
            return emptyList()
        }
    }
}
