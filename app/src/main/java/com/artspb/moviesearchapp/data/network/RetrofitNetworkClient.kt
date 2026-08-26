package com.artspb.moviesearchapp.data.network

import com.artspb.moviesearchapp.BuildConfig
import com.artspb.moviesearchapp.data.dto.MoviesSearchRequest
import com.artspb.moviesearchapp.data.dto.Response
import com.artspb.moviesearchapp.ui.inspector.ArchitectureFlowMonitor
import com.artspb.moviesearchapp.ui.inspector.LayerType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Моя реализация сетевого клиента на основе Retrofit.
// Живет в слое Data. Именно здесь инкапсулированы все детали подключения к https://www.omdbapi.com,
// работа с Gson и выполнение HTTP-запросов.
class RetrofitNetworkClient : NetworkClient {

    // Ссылка на сервер OMDb. Использую https, так как Android блокирует незащищенный трафик.
    private val imdbBaseUrl = "https://www.omdbapi.com"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    // Инициализирую Retrofit. Добавляю конвертер Gson, чтобы он превращал JSON в объекты DTO.
    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imdbService = retrofit.create(IMDbApi::class.java)

    override fun doRequest(dto: Any): Response {
        // Проверяю, является ли переданный объект запросом на поиск фильмов.
        if (dto is MoviesSearchRequest) {
            // Отправляю событие в ArchitectureFlowMonitor, чтобы подсветить HUD-индикатор и записать лог (Вау-эффект для портфолио).
            ArchitectureFlowMonitor.logStep(
                layer = LayerType.DATA,
                title = "RetrofitNetworkClient.doRequest()",
                details = "Синхронный сетевой запрос (execute) к OMDb API",
                payloadPreview = "DTO Request: MoviesSearchRequest(expression = '${dto.expression}')"
            )

            return try {
                // ВАЖНО: В отличие от старого кода в Activity, где был enqueue(), здесь по правилам Clean Architecture
                // я вызываю execute() для синхронного выполнения запроса, так как метод doRequest() вызывается 
                // из фонового потока, созданного в Interactor!
                val resp = imdbService.findMovie(BuildConfig.OMDB_API_KEY, dto.expression).execute()
                val body = resp.body() ?: return Response().apply { resultCode = 500 }

                ArchitectureFlowMonitor.logStep(
                    layer = LayerType.DATA,
                    title = "Ответ сервера OMDb (HTTP ${resp.code()})",
                    details = "Получен JSON от сервера и автоматически сконвертирован Gson в DTO",
                    payloadPreview = "DTO Response: resultCode=${resp.code()}, results count=${(body as? com.artspb.moviesearchapp.data.dto.MoviesSearchResponse)?.results?.size ?: 0}"
                )

                body.apply { resultCode = resp.code() }
            } catch (e: Exception) {
                ArchitectureFlowMonitor.logStep(
                    layer = LayerType.DATA,
                    title = "Ошибка сетевого запроса",
                    details = "Исключение при выполнении запроса: ${e.message}",
                    payloadPreview = "resultCode = 500"
                )
                Response().apply { resultCode = 500 }
            }
        } else {
            // Если передан неизвестный DTO, возвращаю код ошибки 400 (Bad Request).
            ArchitectureFlowMonitor.logStep(
                layer = LayerType.DATA,
                title = "Неверный тип DTO запроса",
                details = "Ожидался MoviesSearchRequest, но передан другой объект",
                payloadPreview = "resultCode = 400"
            )
            return Response().apply { resultCode = 400 }
        }
    }
}
