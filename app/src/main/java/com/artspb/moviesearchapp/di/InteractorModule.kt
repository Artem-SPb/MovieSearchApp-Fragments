package com.artspb.moviesearchapp.di

import com.artspb.moviesearchapp.domain.api.MoviesInteractor
import com.artspb.moviesearchapp.domain.impl.MoviesInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<MoviesInteractor> {
        MoviesInteractorImpl(get())
    }
}