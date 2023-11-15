package com.nishant.core.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nishant.core.network.api.MoviesApiService
import com.nishant.core.network.models.MovieItemDto
import com.nishant.core.network.models.MoviesDto
import com.nishant.core.repo.paging.NowPlayingMoviesDataSource
import com.nishant.core.repo.paging.PopularPagingDataSource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MoviesRepo @Inject constructor(private val apiService: MoviesApiService
        ) : IMoviesRepo {

    override fun search(q : String): Flow<LoadingState<MoviesDto>> {
        return flow{
            emit(apiService.search(q))
        }.asResult()
    }

    override fun getPopularMovies(genreId:String): Flow<PagingData<MovieItemDto>> {
        return Pager(PagingConfig(pageSize = 20), pagingSourceFactory =
            { PopularPagingDataSource(genreId,apiService) }).flow
    }

    override fun getNowPlayingMovies(genreId: String): Flow<PagingData<MovieItemDto>> {
        return Pager(PagingConfig(20), pagingSourceFactory = {
            NowPlayingMoviesDataSource(genreId,apiService)
        }).flow
    }

    override suspend fun getMovieDetail(movieId: Int) = apiService.movieDetail(movieId)

    override suspend fun getRecommendedMovies(movieId: Int) = apiService.recommendedMovie(movieId,1)

    override suspend fun getCredits(movieId: Int) = apiService.movieCredit(movieId)

    override suspend fun getArtistDetail(personId: Int) = apiService.artistDetail(personId)


}