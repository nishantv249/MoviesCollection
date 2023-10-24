package com.nishant.moviescollection.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nishant.moviescollection.data.GenreDao
import com.nishant.moviescollection.network.ApiService
import com.nishant.moviescollection.network.Result
import com.nishant.moviescollection.network.asResult
import com.nishant.moviescollection.network.models.*
import com.nishant.moviescollection.repo.paging.NowPlayingMoviesDataSource
import com.nishant.moviescollection.repo.paging.PopularPagingDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepo @Inject constructor(private val apiService: ApiService
        ,private val genreDao: GenreDao) : IMoviesRepo {

    override fun search(q : String ): Flow<Result<BaseModel>> {
        return flow{
            emit(apiService.search(q))
        }.asResult()
    }

    override fun getGenres(): Flow<Result<Genres>> {
        return genreDao.getAll().map {
            try {
                if(it.isEmpty()){
                    val result = apiService.genreList()
                    if(result.genres.isEmpty()){
                        Result.Error("")
                    }else{
                        withContext(Dispatchers.IO){ genreDao.upsert(result.genres)}
                        Result.Loading
                    }
                }else{
                    Result.Success(Genres(it))
                }
            }catch (e : Exception){
                Result.Error(e.message.toString())
            }
        }
    }

    override fun getPopularMovies(genreId:String): Flow<PagingData<MovieItem>> {
        return Pager(PagingConfig(pageSize = 20), pagingSourceFactory =
            {PopularPagingDataSource(genreId,apiService)}).flow
    }

    override fun getNowPlayingMovies(genreId: String): Flow<PagingData<MovieItem>> {
        return Pager(PagingConfig(20), pagingSourceFactory = {
            NowPlayingMoviesDataSource(genreId,apiService)
        }).flow
    }

    override suspend fun getMovieDetail(movieId: Int) = apiService.movieDetail(movieId)

    override suspend fun getRecommendedMovies(movieId: Int) = apiService.recommendedMovie(movieId,1)

    override suspend fun getCredits(movieId: Int) = apiService.movieCredit(movieId)

    override suspend fun getArtistDetail(personId: Int) = apiService.artistDetail(personId)


}