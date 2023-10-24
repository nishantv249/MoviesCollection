package com.nishant.moviescollection.ui.screens.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.moviescollection.network.Result
import com.nishant.moviescollection.network.asResult
import com.nishant.moviescollection.network.models.ArtistDetail
import com.nishant.moviescollection.repo.IMoviesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(private  val moviesRepo: IMoviesRepo) : ViewModel() {

    private val _artistDetailFlow : MutableStateFlow<ArtistDetail?> = MutableStateFlow(null)

    val artistDetailFlow = _artistDetailFlow.filter { it != null }.asResult().stateIn(
        viewModelScope, SharingStarted.Eagerly,Result.Empty
    )

    fun getArtistDetail(id : Int){
        viewModelScope.launch {
            _artistDetailFlow.value = moviesRepo.getArtistDetail(id)
        }
    }

}