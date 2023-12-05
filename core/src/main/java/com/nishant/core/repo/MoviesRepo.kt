package com.nishant.core.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nishant.core.data.mediator.NowPlayingItemsMediator
import com.nishant.core.db.MoviesDb
import com.nishant.core.db.dao.NowPlayingMovieItemDao
import com.nishant.core.network.api.MoviesApiService
import com.nishant.core.network.models.MovieItemDto
import com.nishant.core.network.models.MoviesDto
import com.nishant.core.repo.paging.MoviesDataSource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MoviesRepo @Inject constructor(
    private val apiService: MoviesApiService
) : IMoviesRepo {

    override fun search(q : String): Flow<LoadingState<MoviesDto>> {
        return flow{
            emit(apiService.search(q))
        }.asResult()
    }

    override fun getPopularMovies(genreId:String): Flow<PagingData<MovieItemDto>> {
        return Pager(PagingConfig(pageSize = 20)) {
            MoviesDataSource { page ->
                apiService.getPopularMovies(page, genreId).results
            }
        }.flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getNowPlayingMovies(genreId: String): Flow<PagingData<MovieItemDto>> {
        return Pager(PagingConfig(20)) {
            MoviesDataSource { page ->
                apiService.nowPlayingMovieList(page, genreId).results
            }
        }.flow
    }

    override fun getTopRatedMovies(genreId: String): Flow<PagingData<MovieItemDto>> {
        return Pager(PagingConfig(20)){
            MoviesDataSource{page ->
                apiService.topRatedMovieList(page,genreId).results
            }
        }.flow
    }

    override fun getUpcomingMovies(genreId: String): Flow<PagingData<MovieItemDto>> {
        return Pager(PagingConfig(20)){
            MoviesDataSource{page ->
                apiService.getUpcomingMovies(page,genreId).results
            }
        }.flow
    }

    override fun getMovieDetail(movieId: Int) =
        flow {
            emit(apiService.movieDetail(movieId))
        }.asResult()

    override fun getRecommendedMovies(movieId: Int) =
        flow {
            emit(apiService.recommendedMovie(movieId, 1))
        }.asResult()

    override fun getCredits(movieId: Int) =
        flow {
            emit(apiService.movieCredit(movieId))
        }.asResult()

    override fun getArtistDetail(personId: Int) =
        flow{
            emit(apiService.artistDetail(personId))
        }.asResult()

}