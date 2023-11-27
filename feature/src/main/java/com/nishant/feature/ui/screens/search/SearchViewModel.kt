package com.nishant.feature.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.core.network.models.MoviesDto
import com.nishant.core.repo.IMoviesRepo
import com.nishant.core.repo.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class SearchViewModel @Inject constructor(private val moviesRepo: IMoviesRepo) : ViewModel() {

    private val searchRequestFlow : MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResultFlow : StateFlow<LoadingState<MoviesDto>> = searchRequestFlow.debounce(300)
        .distinctUntilChanged()
        .flatMapLatest {
            if(it.isNotEmpty()) {
                moviesRepo.search(it)
            }else{
                flowOf(LoadingState.Empty)
            }
        }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(
            Duration.ZERO), LoadingState.Empty)

    fun search(text: String){
        searchRequestFlow.value = text
    }

}

