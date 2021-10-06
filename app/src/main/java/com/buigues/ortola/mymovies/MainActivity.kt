package com.buigues.ortola.mymovies

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.buigues.ortola.mymovies.databinding.ActivityMainBinding
import com.buigues.ortola.mymovies.model.Movie
import com.buigues.ortola.mymovies.model.MovieDbClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val moviesAdapter = MoviesAdapter(emptyList()){ movie -> navigateTo(movie) }
    // From Google Play Services, to obtain the latest location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    // Aquí guardamos el lanzador de peticiones de permisos, para registrarse en los eventos
    // Siempre se debe hacer antes del onCreate
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
        requestPermissionGranted(isGranted)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FusedLocationProviderClient initialization
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Inicializamos un objeto MoviesAdapter con una lista vacia y lo vinculamos a la view de la UI

        binding.moviesRecycler.adapter = moviesAdapter
        // Aqui se lanza la petición de permisos
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

    }

    private fun navigateTo(movie: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie)
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    private fun requestPermissionGranted(isLocationGranted: Boolean) {

        if (isLocationGranted){
            fusedLocationProviderClient.lastLocation.addOnCompleteListener{ location ->
                doRequestPopularMovies(getRegionFromLocation(location.result))
            }
        }
        else doRequestPopularMovies("ES")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun doRequestPopularMovies(region:String){
        thread {
            val apiKey:String = this.resources.getString(R.string.api_key)
            val popularMovies = MovieDbClient.service.getPopularMovies(apiKey,region)
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

    private fun getRegionFromLocation(location: Location?): String {

        if (location == null) return "ES"

        val geocoder: Geocoder = Geocoder(this)
        val result = geocoder.getFromLocation(location.latitude, location.longitude,1)
        return result.firstOrNull()?.countryCode ?: "ES"
    }
}