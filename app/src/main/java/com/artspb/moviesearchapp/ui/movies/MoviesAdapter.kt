package com.artspb.moviesearchapp.ui.movies

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artspb.moviesearchapp.domain.models.Movie

// Мой адаптер для RecyclerView на слое UI.
// Отрисовывает элементы, используя чистые Domain-модели Movie и передавая клик по карточке во вьюхолдер.
class MoviesAdapter : RecyclerView.Adapter<MovieViewHolder>() {

    var movies = ArrayList<Movie>()
    var onMovieClick: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position], onMovieClick)
    }

    override fun getItemCount(): Int = movies.size
}
