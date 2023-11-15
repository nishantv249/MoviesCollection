package com.nishant.core.data

import com.nishant.core.db.dao.GenreDao
import com.nishant.core.db.entity.GenreEntity
import com.nishant.core.network.api.MoviesApiService
import com.nishant.core.network.models.GenreDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflineFirstGenreRepo @Inject constructor(private val genreDao: GenreDao,
                                                private val apiService: MoviesApiService,
                                                private val dispatcher : CoroutineDispatcher =
                                                    Dispatchers.IO) : GenreRepository {
    override fun getGenre(): Flow<List<GenreEntity>> {
        return genreDao.getAllGenres().map {
            if(it.isEmpty()) {
                withContext(dispatcher) {
                    genreDao.insert(apiService.genreList().genres.map { genreDto -> genreDto.toEntity() })
                }
            }
            it
        }
    }
}

private fun GenreDto.toEntity() : GenreEntity{
    return GenreEntity(id = id,name = name)
}
