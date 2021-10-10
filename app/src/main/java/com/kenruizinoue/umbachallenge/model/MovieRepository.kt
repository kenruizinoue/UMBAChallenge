package com.kenruizinoue.umbachallenge.model

import com.kenruizinoue.umbachallenge.BuildConfig
import com.kenruizinoue.umbachallenge.model.local.MovieDao
import com.kenruizinoue.umbachallenge.model.network.ApiService
import com.kenruizinoue.umbachallenge.model.network.Movies
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class MovieRepository (private val movieDao: MovieDao, private val apiService: ApiService) {

    suspend fun insertLocalMovies(movies: List<Movie>) {
        return movieDao.insertMovies(movies)
    }

    fun getLocalMovies(type: String): Flow<List<Movie>> {
        return movieDao.getMoviesByType(type)
    }

    suspend fun deleteLocalMovies(type: String) {
        return movieDao.deleteMoviesByType(type)
    }

    suspend fun getRemoteMovies(type: String): Response<Movies> {
        return apiService.getMovies(type, BuildConfig.API_KEY)
    }

    suspend fun getRemoteMovie(): Response<Movie> {
        return apiService.getMovie(BuildConfig.API_KEY)
    }
}