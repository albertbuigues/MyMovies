package com.buigues.ortola.mymovies

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.buigues.ortola.mymovies.databinding.ActivityMainBinding
import com.buigues.ortola.mymovies.model.Movie
import com.buigues.ortola.mymovies.model.MovieDbClient
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_MOVIE = "MovieDetailActivity:movie"
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos un objeto MoviesAdapter con una lista vacia y lo vinculamos a la view de la UI
        val moviesAdapter = MoviesAdapter(emptyList()){ movie -> navigateTo(movie) }
        binding.moviesRecycler.adapter = moviesAdapter

        thread {
            val apiKey:String = this.resources.getString(R.string.api_key)
            val popularMovies = MovieDbClient.service.getPopularMovies(apiKey)
            val body = popularMovies.execute().body()

            runOnUiThread {
                // En el hilo secundario rellenamos esa lista con el body.movies y notificamos el cambio.
                if (body != null) {
                    moviesAdapter.moviesList = body.movies
                    moviesAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun navigateTo(movie: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie)
        startActivity(intent)
    }
}