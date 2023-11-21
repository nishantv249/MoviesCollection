@file:Suppress("AndroidUnresolvedRoomSqlReference")

package com.nishant.core.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.nishant.core.db.entity.NowPlayingMovieItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NowPlayingMovieItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNowPlayingMovieItems(nowPlayingMovieItemEntities: List<NowPlayingMovieItemEntity>)


    @Query("DELETE FROM NowPlayingMovieItemEntity")
    suspend fun deleteAll()

    @Query("Select * from NowPlayingMovieItemEntity")
    fun getPagingSource() : PagingSource<Int,NowPlayingMovieItemEntity>
}