package com.buigues.ortola.mymovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.buigues.ortola.mymovies.databinding.ActivityMainBinding
import com.buigues.ortola.mymovies.databinding.ActivityMovieDetailBinding
import com.buigues.ortola.mymovies.model.Movie
import com.bumptech.glide.Glide

class MovieDetailActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_MOVIE = "MovieDetailActivity:movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        if (movie != null){
            title = movie.title
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w780/${movie.backdrop_path}")
                .into(binding.movieDetailBackdrop)
            binding.movieDetailSummary.text = movie.overview
            bindDetailInfo(binding.movieDetailInfo, movie)
        }
    }

    private fun bindDetailInfo(movieDetailInfo: TextView, movie: Movie) {
        movieDetailInfo.text = buildSpannedString {
            bold { append("Original Language: ") }
            appendLine(movie.original_language)

            bold { append("Original Title: ") }
            appendLine(movie.original_title)

            bold { append("Release Date: ") }
            appendLine(movie.release_date)

            bold { append("Popularity: ") }
            appendLine(movie.popularity.toString())

            bold { append("Vote Average: ") }
            appendLine(movie.vote_average.toString())
        }
    }
}