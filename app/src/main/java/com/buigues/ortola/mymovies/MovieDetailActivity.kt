package com.buigues.ortola.mymovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
            appendInformation(this, R.string.original_language, movie.original_language)
            appendInformation(this, R.string.original_title, movie.title)
            appendInformation(this, R.string.release_date, movie.release_date)
            appendInformation(this, R.string.popularity, movie.popularity.toString())
            appendInformation(this, R.string.vote_average, movie.vote_average.toString())


            /*bold { append("Original Title: ") }
            appendLine(movie.original_title)

            bold { append("Release Date: ") }
            appendLine(movie.release_date)

            bold { append("Popularity: ") }
            appendLine(movie.popularity.toString())

            bold { append("Vote Average: ") }
            appendLine(movie.vote_average.toString())*/
        }
    }

    private fun appendInformation(builder: SpannableStringBuilder, stringReference:Int, value:String){
        builder.bold {
            append(getString(stringReference))
            append(": ")
        }
        builder.appendLine(value)
    }
}