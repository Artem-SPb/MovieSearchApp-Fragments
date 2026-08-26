package com.artspb.moviesearchapp.ui.poster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.artspb.moviesearchapp.ui.inspector.ArchitectureFlowMonitor
import com.artspb.moviesearchapp.ui.inspector.LayerType

class PosterViewModel(private val posterUrl: String) : ViewModel() {

    companion object {
        fun getFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PosterViewModel(url)
            }
        }
    }

    private val urlLiveData = MutableLiveData(posterUrl)
    fun observeUrl(): LiveData<String> = urlLiveData
    
    init {
        ArchitectureFlowMonitor.logStep(
            layer = LayerType.VIEW_MODEL,
            title = "PosterViewModel.init",
            details = "Инициализация PosterViewModel и сохранение URL в LiveData",
            payloadPreview = "posterUrl = $posterUrl"
        )
    }
}
