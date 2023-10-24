package com.nishant.moviescollection.repo

import androidx.paging.PagingData
import com.nishant.moviescollection.network.Result
import com.nishant.moviescollection.network.models.*
import kotlinx.coroutines.flow.Flow

interface IMoviesRepo {

    fun search(q : String ) : Flow<Result<BaseModel>>

    fun getGenres() : Flow<Result<Genres>>

    fun getPopularMovies(genreId:String) : Flow<PagingData<MovieItem>>

    fun getNowPlayingMovies(genreId: String) :Flow<PagingData<MovieItem>>

    suspend fun getMovieDetail(movieId : Int) : MovieDetail

    suspend fun getRecommendedMovies(movieId : Int) : BaseModel

    suspend fun getCredits(movieId: Int) : Artist

    suspend fun getArtistDetail(personId : Int) : ArtistDetail

}