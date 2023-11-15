package com.nishant.core.repo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nishant.core.network.api.MoviesApiService
import com.nishant.core.network.models.MovieItemDto

class NowPlayingMoviesDataSource(private val genreId : String, private val apiService: MoviesApiService)
    : PagingSource<Int,MovieItemDto>() {

    override fun getRefreshKey(state: PagingState<Int, MovieItemDto>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItemDto> {
        return try{
            val page = params.key?:1
            val pageList =   apiService.nowPlayingMovieList(page,genreId)
          LoadResult.Page(pageList.results,if(page == 1) null else page-1,pageList.page+1)
        }catch(e : Exception) {
            LoadResult.Error(e)
        }
    }
}