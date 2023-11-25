package com.nishant.core.repo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nishant.core.network.api.MoviesApiService
import com.nishant.core.network.models.MovieItemDto

class NowPlayingMoviesDataSource(private val genreId : String, private val apiService: MoviesApiService)
    : MoviesDataSource() {

    override suspend fun getMovies(page: Int): List<MovieItemDto> {
        return apiService.nowPlayingMovieList(page = page,genreId).results
    }

}