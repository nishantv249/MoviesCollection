package com.nishant.core.repo

import androidx.paging.PagingData
import com.nishant.core.network.models.ArtistDetailDto
import com.nishant.core.network.models.ArtistDto
import com.nishant.core.network.models.MovieDetailDto
import com.nishant.core.network.models.MovieItemDto
import com.nishant.core.network.models.MoviesDto
import kotlinx.coroutines.flow.Flow

interface IMoviesRepo {

    fun search(q : String ) : Flow<LoadingState<MoviesDto>>

    fun getPopularMovies(genreId:String) : Flow<PagingData<MovieItemDto>>

    fun getNowPlayingMovies(genreId: String) :Flow<PagingData<MovieItemDto>>

    suspend fun getMovieDetail(movieId : Int) : MovieDetailDto

    suspend fun getRecommendedMovies(movieId : Int) : MoviesDto

    suspend fun getCredits(movieId: Int) : ArtistDto

    suspend fun getArtistDetail(personId : Int) : ArtistDetailDto

}