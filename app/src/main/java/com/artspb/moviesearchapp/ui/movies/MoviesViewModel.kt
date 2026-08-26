package com.artspb.moviesearchapp.ui.movies

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.artspb.moviesearchapp.Creator
import com.artspb.moviesearchapp.R
import com.artspb.moviesearchapp.domain.api.MoviesInteractor
import com.artspb.moviesearchapp.domain.models.Movie
import com.artspb.moviesearchapp.ui.inspector.ArchitectureFlowMonitor
import com.artspb.moviesearchapp.ui.inspector.LayerType

class MoviesViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as Application
                MoviesViewModel(app)
            }
        }
    }

    private val moviesInteractor = Creator.provideMoviesInteractor()

    private val stateLiveData = MutableLiveData<MoviesState>()
    fun observeState(): LiveData<MoviesState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    private var latestSearchText: String? = null

    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        ArchitectureFlowMonitor.logStep(
            layer = LayerType.VIEW_MODEL,
            title = "MoviesViewModel.searchDebounce()",
            details = "Отложенный запуск поиска с использованием debounce",
            payloadPreview = "query = '$changedText'"
        )

        val searchRunnable = Runnable { searchRequest(changedText) }

        handler.postDelayed(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            SEARCH_DEBOUNCE_DELAY
        )
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            
            ArchitectureFlowMonitor.logStep(
                layer = LayerType.VIEW_MODEL,
                title = "MoviesViewModel.searchRequest()",
                details = "Изменение LiveData состояния на Loading и запрос в Interactor",
                payloadPreview = "renderState(MoviesState.Loading)"
            )

            renderState(MoviesState.Loading)

            moviesInteractor.searchMovies(newSearchText, object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>) {
                    handler.post {
                        val movies = mutableListOf<Movie>()
                        movies.addAll(foundMovies)

                        ArchitectureFlowMonitor.logStep(
                            layer = LayerType.VIEW_MODEL,
                            title = "MoviesViewModel (Callback received)",
                            details = "Получен ответ от Interactor, обновление LiveData",
                            payloadPreview = "movies.size = ${movies.size}"
                        )

                        if (movies.isEmpty()) {
                            renderState(
                                MoviesState.Empty(
                                    message = getApplication<Application>().getString(R.string.nothing_found)
                                )
                            )
                        } else {
                            renderState(
                                MoviesState.Content(
                                    movies = movies
                                )
                            )
                        }
                    }
                }
            })
        }
    }

    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
}
