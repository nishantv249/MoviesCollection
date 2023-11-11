package com.nishant.moviescollection.ui.screens.search

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishant.moviescollection.network.Result
import com.nishant.moviescollection.network.models.BaseModel
import com.nishant.moviescollection.repo.IMoviesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class SearchViewModel @Inject constructor(private val moviesRepo: IMoviesRepo) : ViewModel() {

    private val searchRequestFlow : MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val flow : StateFlow<Result<BaseModel>> = searchRequestFlow.debounce(300)
        .distinctUntilChanged()
        .flatMapLatest {
            if(it.isNotEmpty()) {
                moviesRepo.search(it)
            }else{
                flow {
                    emit(Result.Empty)
                }
            }
        }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(
            Duration.ZERO), Result.Empty)

    fun search(text: String){
            searchRequestFlow.value = text
    }

}

