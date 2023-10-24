package com.nishant.moviescollection.repo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nishant.moviescollection.network.ApiService
import com.nishant.moviescollection.network.models.MovieItem

class NowPlayingMoviesDataSource(private val genreId : String, private val apiService: ApiService) : PagingSource<Int,MovieItem>() {

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        return try{
            val page = params.key?:1
            val pageList = apiService.nowPlayingMovieList(page,genreId)
          LoadResult.Page(pageList.results,if(page == 1) null else page-1,pageList.page+1)
        }catch(e : Exception) {
            LoadResult.Error(e)
        }
    }
}