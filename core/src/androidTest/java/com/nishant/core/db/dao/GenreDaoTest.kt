package com.nishant.core.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.nishant.core.db.MoviesDb
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.nishant.core.db.entity.GenreEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle


@RunWith(Androi::class)
class GenreDaoTest {

    private lateinit var genreDao: GenreDao

    @Before
    fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db  = Room.inMemoryDatabaseBuilder(context,MoviesDb::class.java).build()
        genreDao = db.getGenreDao()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertGenre() = runTest{
        genreDao.insert(emptyList())
        advanceUntilIdle()
        assertThat(genreDao.getAllGenres().first()).isEqualTo(emptyList<GenreEntity>())
    }

}