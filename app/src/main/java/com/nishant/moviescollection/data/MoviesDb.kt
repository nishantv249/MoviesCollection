package com.nishant.moviescollection.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nishant.moviescollection.network.models.Genre

@Database( entities = [ Genre::class], version = 1)
abstract class MoviesDb : RoomDatabase() {

    abstract fun getGenreDao() : GenreDao
}