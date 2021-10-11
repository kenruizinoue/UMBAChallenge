package com.kenruizinoue.umbachallenge.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kenruizinoue.umbachallenge.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert
    suspend fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM Movie WHERE type = :type")
    fun getMoviesByType(type: String): List<Movie>

    @Query("DELETE FROM Movie WHERE type = :type")
    suspend fun deleteMoviesByType(type: String)
}