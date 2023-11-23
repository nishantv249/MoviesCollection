package com.nishant.core.repo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nishant.core.network.models.MovieItemDto

abstract class MoviesDataSource : PagingSource<Int,MovieItemDto>() {

    override fun getRefreshKey(state: PagingState<Int, MovieItemDto>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItemDto> {
        val page = params.key ?: 1
        return try {
            val movies = getMovies(page)
            println(movies)
            LoadResult.Page(movies,if(page == 1) null else page-1,page+1)
        }catch (e : Exception) {
            LoadResult.Error(e)
        }
    }

    abstract suspend fun getMovies(page: Int): List<MovieItemDto>


}