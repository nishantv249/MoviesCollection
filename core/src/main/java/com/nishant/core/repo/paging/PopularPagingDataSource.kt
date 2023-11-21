package com.nishant.core.repo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nishant.core.network.api.MoviesApiService
import com.nishant.core.network.models.MovieItemDto
import javax.inject.Inject

class PopularPagingDataSource @Inject constructor(private val genreId:String,private val
                                        apiService: MoviesApiService) : MoviesDataSource() {
    override suspend fun getMovies(page: Int): List<MovieItemDto> {
        return apiService.getPopularMovies(page,genreId).results
    }

}