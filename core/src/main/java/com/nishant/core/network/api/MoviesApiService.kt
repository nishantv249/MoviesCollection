package com.nishant.core.network.api

import com.nishant.core.network.models.ArtistDetailDto
import com.nishant.core.network.models.ArtistDto
import com.nishant.core.network.models.GenresDto
import com.nishant.core.network.models.MovieDetailDto
import com.nishant.core.network.models.MoviesDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {

    @GET("search/movie?page=1&include_adult=false")
    suspend fun search(
        @Query("query") searchKey: String, @Query("api_key") apiKey: String = API_KEY
    ): MoviesDto

    @GET("genre/movie/list")
    suspend fun genreList(@Query("api_key") apiKey: String = API_KEY): GenresDto

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("with_genres") genreId: String?,
        @Query("api_key") apiKey: String = API_KEY
    ): MoviesDto

    @GET("movie/now_playing")
    suspend fun nowPlayingMovieList(
        @Query("page") page: Int,
        @Query("with_genres") genreId: String?,
        @Query("api_key") apiKey: String = API_KEY
    ): MoviesDto

    @GET("movie/top_rated")
    suspend fun topRatedMovieList(
        @Query("page") page: Int,
        @Query("with_genres") genreId: String?,
        @Query("api_key") apiKey: String = API_KEY
    ): MoviesDto

    @GET("movie/upcoming")
    suspend fun upcomingMovieList(
        @Query("page") page: Int,
        @Query("with_genres") genreId: String?,
        @Query("api_key") apiKey: String = API_KEY
    ): MoviesDto


    @GET("movie/{movieId}")
    suspend fun movieDetail(
        @Path("movieId") movieId: Int, @Query("api_key") apiKey: String = API_KEY
    ): MovieDetailDto

    @GET("movie/{movieId}/recommendations")
    suspend fun recommendedMovie(
        @Path("movieId") movieId: Int,
        @Query("page") one: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MoviesDto

    @GET("movie/{movieId}/credits")
    suspend fun movieCredit(
        @Path("movieId") movieId: Int, @Query("api_key") apiKey: String = API_KEY
    ): ArtistDto

    @GET("person/{personId}")
    suspend fun artistDetail(
        @Path("personId") personId: Int, @Query("api_key") apiKey: String = API_KEY
    ): ArtistDetailDto

    companion object{
        const val API_KEY = "59cd6896d8432f9c69aed9b86b9c2931"
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w342"
    }

}