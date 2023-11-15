package com.nishant.core.data

import com.nishant.core.db.entity.GenreEntity
import kotlinx.coroutines.flow.Flow

interface GenreRepository {

    fun getGenre() : Flow<List<GenreEntity>>

}