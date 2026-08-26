package com.artspb.moviesearchapp

import com.artspb.moviesearchapp.data.MoviesRepositoryImpl
import com.artspb.moviesearchapp.data.network.RetrofitNetworkClient
import com.artspb.moviesearchapp.domain.api.MoviesInteractor
import com.artspb.moviesearchapp.domain.api.MoviesRepository
import com.artspb.moviesearchapp.domain.impl.MoviesInteractorImpl

// Мой локатор зависимостей (Creator) — объект, связывающий все слои воедино.
// Он инициализирует репозиторий и сетевой клиент и отдает в UI-слой готовый экземпляр MoviesInteractor.
// Благодаря этому MoviesActivity не знает, как именно создается репозиторий и какой клиент используется.
object Creator {
    private fun getMoviesRepository(): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMoviesInteractor(): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository())
    }
}
