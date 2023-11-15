package com.nishant.feature.ui.component.drawer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.core.domain.models.Genre
import com.nishant.core.domain.usecases.GenreUseCase
import com.nishant.core.repo.LoadingState
import com.nishant.core.repo.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(private val genreUseCase: GenreUseCase) : ViewModel() {

    val genresState : MutableState<LoadingState<List<Genre>>?> = mutableStateOf(null)

    fun getGenres() {
        viewModelScope.launch {
            genreUseCase().asResult()
                .onEach {
                    genresState.value = it
                }.collect()
        }
    }



}