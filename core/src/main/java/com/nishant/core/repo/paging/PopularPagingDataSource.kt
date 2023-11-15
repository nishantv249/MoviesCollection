package com.nishant.core.repo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nishant.core.network.api.MoviesApiService
import com.nishant.core.network.models.MovieItemDto
import javax.inject.Inject

class PopularPagingDataSource @Inject constructor(private val genreId:String,private val
                                        apiService: MoviesApiService) : PagingSource<Int, MovieItemDto>() {

    override fun getRefreshKey(state: PagingState<Int, MovieItemDto>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItemDto> {
        val nextPage = params.key ?:1

        return try{
            val result = apiService.popularMovieList(nextPage,genreId)
            LoadResult.Page(result.results,if(nextPage == 1) null else nextPage-1,result.page+1)
        }catch (e : Exception){
            LoadResult.Error(e)
        }

    }
}