package com.nishant.core.repo.paging

import com.nishant.core.network.api.MoviesApiService
import com.nishant.core.network.models.MovieItemDto

class UpcomingMoviesDataSource(private val genreId : String,private val moviesApiService:
        MoviesApiService) : MoviesDataSource() {
    override suspend fun getMovies(page: Int): List<MovieItemDto> {
        return moviesApiService.getUpcomingMovies(page,genreId).results
    }

}