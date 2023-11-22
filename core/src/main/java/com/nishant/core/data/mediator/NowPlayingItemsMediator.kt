package com.nishant.core.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.InvalidationTracker
import androidx.room.withTransaction
import com.nishant.core.data.toNowPlayingMovieEntity
import com.nishant.core.datastore.MoviesPrefs
import com.nishant.core.db.MoviesDb
import com.nishant.core.db.entity.NowPlayingMovieItemEntity
import com.nishant.core.network.api.MoviesApiService
import kotlinx.coroutines.delay
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class NowPlayingItemsMediator @Inject constructor(
    private val moviesDb: MoviesDb, private val moviesApiService:
    MoviesApiService, private val moviesPrefs: MoviesPrefs
) : RemoteMediator<Int, NowPlayingMovieItemEntity>() {

    init {
        moviesDb.invalidationTracker.addObserver(object : InvalidationTracker
            .Observer(arrayOf("NowPlayingMovieItemEntity")) {
            override fun onInvalidated(tables: Set<String>) {
                println("invalidated")
            }
        })
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NowPlayingMovieItemEntity>
    ): MediatorResult {
        println("loading with $loadType and ${state.pages}")
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        moviesPrefs.getNowPlayingItemPageCount() + 1
                    }
                }

                else -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = moviesApiService.nowPlayingMovieList(page, "")
            println("$page and $loadType")

            moviesDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    moviesDb.nowPlayingMovieDao().deleteAll()
                }
                moviesPrefs.setNowPlayingItemPageCount(page)
                moviesDb.nowPlayingMovieDao().insertNowPlayingMovieItems(response.results.map {
                    it.toNowPlayingMovieEntity()
                })
            }

            MediatorResult.Success(false)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

}