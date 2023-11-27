package com.nishant.core.repo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nishant.core.network.models.MovieItemDto
import kotlinx.coroutines.CancellationException

class MoviesDataSource (private val getMovies : suspend (page : Int) -> List<MovieItemDto>) : PagingSource<Int,MovieItemDto>() {

    override fun getRefreshKey(state: PagingState<Int, MovieItemDto>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItemDto> {
        val page = params.key ?: 1
        return try {
            val movies = getMovies(page)
            LoadResult.Page(movies,if(page == 1) null else page-1,page+1)
        }catch (e : Exception) {
            if(e is CancellationException){
                throw e
            }
            LoadResult.Error(e)
        }
    }

}