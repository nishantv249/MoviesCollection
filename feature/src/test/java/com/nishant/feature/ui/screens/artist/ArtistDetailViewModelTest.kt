package com.nishant.feature.ui.screens.artist

import com.nishant.core.network.models.ArtistDetailDto
import com.nishant.core.repo.IMoviesRepo
import com.nishant.core.repo.LoadingState
import com.nishant.feature.MainCoroutineRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.IOException
import java.lang.RuntimeException

class ArtistDetailViewModelTest {

    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var iMoviesRepo: IMoviesRepo

    private lateinit var artistDetailViewModel: ArtistDetailViewModel

    private val personId = 1

    private val errorMsg = "no network"

    private val artistDetail = ArtistDetailDto(false, emptyList(),"her test biography",
                                        "24/02/90","",1,
                                    "",1,"","department",
                                        "riyu","any",1.2,"profilePath")

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        artistDetailViewModel = ArtistDetailViewModel(iMoviesRepo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getArtistDetailFlow() = runTest{
        Mockito.`when`(iMoviesRepo.getArtistDetail(personId)).thenReturn(flowOf(LoadingState.Success(
            artistDetail)))
        var artistDetailCollected : LoadingState<ArtistDetailDto?>? = null
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            artistDetailCollected = artistDetailViewModel.artistDetailFlow.drop(1).first()
        }
        artistDetailViewModel.getArtistDetail(personId)
        assertThat(artistDetailCollected).isEqualTo(LoadingState.Success(artistDetail))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getArtistDetailFlowOnError() = runTest{
        Mockito.`when`(iMoviesRepo.getArtistDetail(personId))
            .thenReturn(flowOf(LoadingState.Error(errorMsg)))
        var artistDetailCollected : LoadingState<ArtistDetailDto?>? = null
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            artistDetailCollected = artistDetailViewModel.artistDetailFlow.drop(1).first()
        }
        artistDetailViewModel.getArtistDetail(personId)
        assertThat(artistDetailCollected).isEqualTo(LoadingState.Error(errorMsg))
    }

}