package com.artspb.moviesearchapp.di

import com.artspb.moviesearchapp.data.MoviesRepositoryImpl
import com.artspb.moviesearchapp.data.network.NetworkClient
import com.artspb.moviesearchapp.data.network.RetrofitNetworkClient
import com.artspb.moviesearchapp.domain.api.MoviesRepository
import org.koin.dsl.module

val dataModule = module {
    single<NetworkClient> {
        RetrofitNetworkClient()
    }
    single<MoviesRepository> {
        MoviesRepositoryImpl(get())
    }
}