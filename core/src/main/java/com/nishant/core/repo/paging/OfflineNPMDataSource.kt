package com.nishant.core.repo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.InvalidationTracker
import androidx.room.paging.LimitOffsetDataSource
import com.nishant.core.db.MoviesDb
import com.nishant.core.db.entity.NowPlayingMovieItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflineNPMDataSource @Inject constructor(private val moviesDb: MoviesDb) :
    PagingSource<Int, NowPlayingMovieItemEntity>() {

    init {
        //To Do remove the observer when done
        moviesDb.invalidationTracker.addObserver(object :InvalidationTracker.Observer(arrayOf("NowPlayingMovieItemEntity")){
            override fun onInvalidated(tables: Set<String>) {
                invalidate()
            }
        })
    }

    override fun getRefreshKey(state: PagingState<Int, NowPlayingMovieItemEntity>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NowPlayingMovieItemEntity> {

        /*return try {
            withContext(Dispatchers.IO) {
                val page = params.key ?: 1
                LoadResult.Page(
                    moviesDb.nowPlayingMovieDao().getAll(), if (page == 1) null else page - 1,
                    page + 1
                )
            }
        }catch (e : Exception){
            LoadResult.Error(e)
        }

*/
    return LoadResult.Error(RuntimeException(""))
    }

}