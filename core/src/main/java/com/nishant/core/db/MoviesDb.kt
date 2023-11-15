package com.nishant.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nishant.core.db.dao.GenreDao
import com.nishant.core.db.entity.GenreEntity


@Database(entities = [GenreEntity::class], version = 1)
abstract class MoviesDb : RoomDatabase() {

    abstract fun getGenreDao() : GenreDao

}