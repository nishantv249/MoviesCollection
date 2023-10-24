package com.nishant.moviescollection.ui.screens

import androidx.lifecycle.ViewModel
import com.nishant.moviescollection.repo.IMoviesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor (private val moviesRepo: IMoviesRepo) : ViewModel() {


}