package com.artspb.moviesearchapp.ui.poster

import com.artspb.moviesearchapp.domain.models.MovieDetails

sealed interface AboutState {
    data class Content(val movie: MovieDetails) : AboutState
    data class Error(val message: String) : AboutState
}