package com.artspb.moviesearchapp.ui.movies

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artspb.moviesearchapp.R
import com.artspb.moviesearchapp.domain.api.MoviesInteractor
import com.artspb.moviesearchapp.domain.models.Movie
import com.artspb.moviesearchapp.ui.inspector.ArchitectureFlowMonitor
import com.artspb.moviesearchapp.ui.inspector.LayerType

class MoviesViewModel(
    application: Application,
    private val moviesInteractor: MoviesInteractor
): AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

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
            details = "Останавливаем поиск и запускаем debounce",
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
                details = "Показываем Loading и вызываем Interactor",
                payloadPreview = "renderState(MoviesState.Loading)"
            )

            renderState(MoviesState.Loading)

            moviesInteractor.searchMovies(newSearchText, object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                    handler.post {
                        ArchitectureFlowMonitor.logStep(
                            layer = LayerType.VIEW_MODEL,
                            title = "MoviesViewModel (Callback received)",
                            details = "Ответ от Interactor",
                            payloadPreview = "movies.size = ${foundMovies?.size ?: 0}"
                        )

                        if (foundMovies != null) {
                            if (foundMovies.isEmpty()) {
                                renderState(
                                    MoviesState.Empty(
                                        message = getApplication<Application>().getString(R.string.nothing_found)
                                    )
                                )
                            } else {
                                renderState(
                                    MoviesState.Content(
                                        movies = foundMovies
                                    )
                                )
                            }
                        } else {
                            renderState(
                                MoviesState.Error(
                                    errorMessage = errorMessage ?: getApplication<Application>().getString(R.string.something_went_wrong)
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
