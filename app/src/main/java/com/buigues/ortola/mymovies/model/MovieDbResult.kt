package com.buigues.ortola.mymovies.model

import com.google.gson.annotations.SerializedName

data class MovieDbResult(
    val page: Int,
    @SerializedName("results")
    val movies: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)