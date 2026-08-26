package com.artspb.moviesearchapp

import android.app.Application
import com.artspb.moviesearchapp.di.dataModule
import com.artspb.moviesearchapp.di.interactorModule
import com.artspb.moviesearchapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Я инициализирую здесь Koin вместо использования самописного Creator.kt,
        // чтобы сделать DI более чистым и масштабируемым
        startKoin {
            androidContext(this@MoviesApplication)
            modules(dataModule, interactorModule, viewModelModule)
        }
    }
}
