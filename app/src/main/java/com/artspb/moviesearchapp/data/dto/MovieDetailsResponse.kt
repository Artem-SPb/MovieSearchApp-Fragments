package com.artspb.moviesearchapp.data.dto

import com.google.gson.annotations.SerializedName

class MovieDetailsResponse(
    @SerializedName("imdbID") val id: String? = "",
    @SerializedName("Title") val title: String? = "",
    @SerializedName("imdbRating") val imDbRating: String? = "",
    @SerializedName("Year") val year: String? = "",
    @SerializedName("Country") val countries: String? = "",
    @SerializedName("Genre") val genres: String? = "",
    @SerializedName("Director") val directors: String? = "",
    @SerializedName("Writer") val writers: String? = "",
    @SerializedName("Actors") val stars: String? = "",
    @SerializedName("Plot") val plot: String? = ""
) : Response()
