package com.kenruizinoue.umbachallenge.model.network

import com.kenruizinoue.umbachallenge.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/3/movie/{type}")
    suspend fun getMovies(@Path("type") type: String, @Query("api_key") key: String): Response<Movies>

    @GET("/3/movie/latest")
    suspend fun getLatestMovie(@Query("api_key") key: String): Response<Movie>
}