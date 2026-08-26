package com.artspb.moviesearchapp.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artspb.moviesearchapp.R
import com.artspb.moviesearchapp.domain.models.Movie
import com.bumptech.glide.Glide

// Мой ViewHolder для карточки фильма на слое UI.
// Инфлейт разметки делаю прямо в конструкторе по Best Practices курса.
// Обратите внимание: он принимает чистую модель Movie из слоя Domain, не зная о Gson или Retrofit.
class MovieViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
) {

    private var cover: ImageView = itemView.findViewById(R.id.cover)
    private var title: TextView = itemView.findViewById(R.id.title)
    private var description: TextView = itemView.findViewById(R.id.description)

    fun bind(movie: Movie, onMovieClick: ((Movie) -> Unit)?) {
        // Использую Glide для загрузки постера по URL. Передаю itemView как контекст.
        Glide.with(itemView)
            .load(movie.image)
            .centerCrop()
            .into(cover)

        title.text = movie.title
        description.text = movie.description

        // Обработчик нажатия на карточку фильма для перехода к деталям (PosterActivity)
        itemView.setOnClickListener {
            onMovieClick?.invoke(movie)
        }
    }
}
