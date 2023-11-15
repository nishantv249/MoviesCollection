package com.nishant.core.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nishant.core.db.entity.GenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Insert
    suspend fun insert(list : List<GenreEntity>)

    @Query("Select * from GenreEntity")
    fun getAllGenres() : Flow<List<GenreEntity>>

}