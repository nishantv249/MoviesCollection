package com.nishant.core.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nishant.core.db.MoviesDb
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.nishant.core.db.entity.GenreEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class GenreDaoTest {

    private lateinit var genreDao: GenreDao

    private val genreEntityList = listOf(GenreEntity(1,"test_genre")
        ,GenreEntity(2,"fiction"))

    @Before
    fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db  = Room.inMemoryDatabaseBuilder(context,MoviesDb::class.java).build()
        genreDao = db.getGenreDao()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertGenre() = runTest{
        genreDao.insert(genreEntityList)
        advanceUntilIdle()
        assertThat(genreDao.getAllGenres().first()).isEqualTo(genreEntityList)
    }

}