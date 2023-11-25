package com.nishant.core.db.dao

import com.nishant.core.db.entity.GenreEntity
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancel
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.coroutineContext

class FakeGenreDao : GenreDao {

    private val genresList = mutableListOf<GenreEntity>()

    private val _genresFlow = MutableStateFlow<List<GenreEntity>>(emptyList())
    override fun getAllGenres(): Flow<List<GenreEntity>> = _genresFlow.asStateFlow()

    override suspend fun insert(list: List<GenreEntity>) {
        genresList.addAll(list)
        _genresFlow.value = genresList.toList()
    }

}