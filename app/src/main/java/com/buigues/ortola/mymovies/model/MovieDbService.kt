package com.buigues.ortola.mymovies.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDbService {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey:String,
        @Query("region") region: String,
    ): Call<MovieDbResult>
}