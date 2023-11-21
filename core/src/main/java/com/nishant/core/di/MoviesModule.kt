package com.nishant.core.di

import android.content.Context
import androidx.room.Room
import com.nishant.core.data.GenreRepository
import com.nishant.core.data.OfflineFirstGenreRepo
import com.nishant.core.data.mediator.NowPlayingItemsMediator
import com.nishant.core.datastore.MoviesPrefs
import com.nishant.core.db.MoviesDb
import com.nishant.core.db.dao.GenreDao
import com.nishant.core.network.api.MoviesApiService
import com.nishant.core.repo.IMoviesRepo
import com.nishant.core.repo.MoviesRepo
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
        return MoviesApiService.BASE_URL
    }

    @Provides
    fun getPreferenceManager(@ApplicationContext context: Context) : MoviesPrefs {
        return MoviesPrefs(context)
    }

    @Provides
    fun getGson() : GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun getClient() : OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    fun getApiService(client: OkHttpClient, url :String, gsonConverterFactory: GsonConverterFactory) : MoviesApiService{
        return Retrofit.Builder().baseUrl(url).client(client).
        addConverterFactory(gsonConverterFactory).build().create(MoviesApiService::class.java)
    }

    @Provides
    fun getDb(@ApplicationContext context: Context) : MoviesDb{
        return Room.databaseBuilder(context.applicationContext,MoviesDb::class.java,
            "movies.db").fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun getGenresDao(moviesDb: MoviesDb ) : GenreDao {
        return moviesDb.getGenreDao()
    }

    @Provides
    fun getNowPlayingItemMediator(moviesDb: MoviesDb,moviesApiService: MoviesApiService,
                                  moviesPrefs: MoviesPrefs) : NowPlayingItemsMediator{
        return NowPlayingItemsMediator(moviesDb,moviesApiService,moviesPrefs)
    }

    @Provides
    fun getRepo(apiService: MoviesApiService,nowPlayingItemsMediator: NowPlayingItemsMediator,moviesDb: MoviesDb) : IMoviesRepo {
        return MoviesRepo(apiService,nowPlayingItemsMediator,moviesDb.nowPlayingMovieDao())
    }

    @Provides
    fun getGenreRepo(genreDao: GenreDao,apiService: MoviesApiService) : GenreRepository {
        return OfflineFirstGenreRepo(genreDao,apiService)
    }



}