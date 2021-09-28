package com.buigues.ortola.mymovies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buigues.ortola.mymovies.databinding.MovieItemBinding
import com.buigues.ortola.mymovies.model.Movie
import com.bumptech.glide.Glide

class MoviesAdapter(var moviesList: List<Movie>,
                    private val movieClickListener: (Movie) -> Unit):
                    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    class ViewHolder(private val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie){
            binding.movieTitle.text = movie.title
           Glide
                .with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w185/${movie.poster_path}")
                .into(binding.movieCover)
        }
    }
    // Creates the view that represents an element of the list and holds several views inside
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MovieItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }
    // Binds the holder with the holder's views data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = moviesList[position]
        holder.bind(moviesList[position])
        // To implement this we must construct a personalized listener for the adapter
        holder.itemView.setOnClickListener { movieClickListener(movie) }
    }
    // Return the number of elements of the adapter
    override fun getItemCount(): Int {
        return moviesList.size
    }
}