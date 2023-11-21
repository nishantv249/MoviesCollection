package com.nishant.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nishant.core.db.dao.GenreDao
import com.nishant.core.db.dao.NowPlayingMovieItemDao
import com.nishant.core.db.entity.GenreEntity
import com.nishant.core.db.entity.NowPlayingMovieItemEntity


@Database(entities = [GenreEntity::class,NowPlayingMovieItemEntity::class], version = 2, exportSchema = false)
abstract class MoviesDb : RoomDatabase() {

    abstract fun getGenreDao() : GenreDao

    abstract fun nowPlayingMovieDao() : NowPlayingMovieItemDao

}