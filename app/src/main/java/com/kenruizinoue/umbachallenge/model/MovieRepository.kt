package com.kenruizinoue.umbachallenge.model

import com.kenruizinoue.umbachallenge.model.local.MovieDao
import kotlinx.coroutines.flow.Flow

class MovieRepository (private val movieDao: MovieDao) {

    suspend fun insertLocalMovies(movies: List<Movie>) {
        return movieDao.insertMovies(movies)
    }

    fun getLocalMovies(type: String): Flow<List<Movie>> {
        return movieDao.getMoviesByType(type)
    }

    suspend fun deleteLocalMovies(type: String) {
        return movieDao.deleteMoviesByType(type)
    }
}