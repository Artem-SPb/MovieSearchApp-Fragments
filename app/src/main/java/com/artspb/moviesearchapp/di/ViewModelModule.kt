package com.artspb.moviesearchapp.di

import com.artspb.moviesearchapp.ui.movies.MoviesViewModel
import com.artspb.moviesearchapp.ui.poster.AboutViewModel
import com.artspb.moviesearchapp.ui.poster.PosterViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MoviesViewModel(androidApplication(), get())
    }
    viewModel { (movieId: String) ->
        AboutViewModel(movieId, get())
    }
    viewModel { (posterUrl: String) ->
        PosterViewModel(posterUrl)
    }
}