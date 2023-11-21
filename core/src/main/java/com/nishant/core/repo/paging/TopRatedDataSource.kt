package com.nishant.core.repo.paging

import com.nishant.core.network.api.MoviesApiService
import com.nishant.core.network.models.MovieItemDto
import javax.inject.Inject

class TopRatedDataSource  @Inject constructor(private val genreId:String, private val apiService:
            MoviesApiService) : MoviesDataSource() {
    override suspend fun getMovies(page: Int): List<MovieItemDto> {
        return apiService.topRatedMovieList(page,genreId).results
    }

}