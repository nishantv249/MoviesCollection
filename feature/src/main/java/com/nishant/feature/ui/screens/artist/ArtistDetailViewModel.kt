package com.nishant.feature.ui.screens.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.core.network.models.ArtistDetailDto
import com.nishant.core.repo.IMoviesRepo
import com.nishant.core.repo.LoadingState
import com.nishant.core.repo.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(private  val moviesRepo: IMoviesRepo) : ViewModel() {

    private val _artistIdFlow : MutableStateFlow<Int?> = MutableStateFlow(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val artistDetailFlow = _artistIdFlow
        .filter { it != null }
        .flatMapLatest { id ->
            moviesRepo.getArtistDetail(id!!)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily,LoadingState.Loading)

    fun getArtistDetail(id : Int){
        viewModelScope.launch {
            _artistIdFlow.value = id
        }
    }

}