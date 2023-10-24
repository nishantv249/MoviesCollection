package com.nishant.moviescollection.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nishant.moviescollection.network.models.Genre
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Upsert
    fun upsert(list : List<Genre>)

    @Query("Select * from Genre")
    fun getAll() : Flow<List<Genre>>

}