package com.kenruizinoue.umbachallenge.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kenruizinoue.umbachallenge.model.Movie

@Database(version = 1, entities = [Movie::class])
abstract class MovieDatabase(): RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}