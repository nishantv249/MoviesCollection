package com.nishant.moviescollection.di

import android.content.Context
import androidx.room.Room
import com.nishant.moviescollection.data.GenreDao
import com.nishant.moviescollection.data.MoviesDb
import com.nishant.moviescollection.network.ApiService
import com.nishant.moviescollection.network.ApiURL
import com.nishant.moviescollection.repo.IMoviesRepo
import com.nishant.moviescollection.repo.MoviesRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object MoviesModule {

    @Provides
    fun getUrl() : String{
        return ApiURL.BASE_URL
    }

    @Provides
    fun getGson() : GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Provides
    fun getClient() : OkHttpClient{
        return OkHttpClient()
    }

    @Provides
    fun getApiService( client: OkHttpClient,url :String,gsonConverterFactory: GsonConverterFactory ) : ApiService{
        return Retrofit.Builder().baseUrl(url).client(client).addConverterFactory(gsonConverterFactory).build().create(ApiService::class.java)
    }

    @Provides
    fun getDb(@ApplicationContext context: Context ) : MoviesDb{
        return Room.databaseBuilder(context.applicationContext,MoviesDb::class.java,"movies.db")
            .build()
    }

    @Provides
    fun getGenresDao(moviesDb: MoviesDb ) : GenreDao{
        return moviesDb.getGenreDao()
    }

    @Provides
    fun getRepo(apiService: ApiService,genreDao: GenreDao) : IMoviesRepo{
        return MoviesRepo(apiService,genreDao)
    }

}