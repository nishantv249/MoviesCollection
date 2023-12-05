package com.nishant.core.repo

import androidx.paging.PagingData
import com.nishant.core.db.entity.NowPlayingMovieItemEntity
import com.nishant.core.network.models.ArtistDetailDto
import com.nishant.core.network.models.ArtistDto
import com.nishant.core.network.models.MovieDetailDto
import com.nishant.core.network.models.MovieItemDto
import com.nishant.core.network.models.MoviesDto
import kotlinx.coroutines.flow.Flow

interface IMoviesRepo {

    fun search(q : String ) : Flow<LoadingState<MoviesDto>>

    fun getPopularMovies(genreId:String) : Flow<PagingData<MovieItemDto>>

    fun getNowPlayingMovies(genreId: String) : Flow<PagingData<MovieItemDto>>

    fun getTopRatedMovies(genreId: String) : Flow<PagingData<MovieItemDto>>

    fun getUpcomingMovies(genreId: String) : Flow<PagingData<MovieItemDto>>

    fun getMovieDetail(movieId : Int) : Flow<LoadingState<MovieDetailDto>>

    fun getRecommendedMovies(movieId : Int) : Flow<LoadingState<MoviesDto>>

    fun getCredits(movieId: Int) : Flow<LoadingState<ArtistDto>>

    fun getArtistDetail(personId : Int) : Flow<LoadingState<ArtistDetailDto>>

}