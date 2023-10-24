package com.nishant.moviescollection.repo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nishant.moviescollection.network.ApiService
import com.nishant.moviescollection.network.models.MovieItem
import javax.inject.Inject

class PopularPagingDataSource @Inject constructor(private val genreId:String,private val
                                        apiService: ApiService) : PagingSource<Int, MovieItem>() {

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        val nextPage = params.key ?:1

        return try{
            val result = apiService.popularMovieList(nextPage,genreId)
            LoadResult.Page(result.results,if(nextPage == 1) null else nextPage-1,result.page+1)
        }catch (e : Exception){
            LoadResult.Error(e)
        }

    }
}