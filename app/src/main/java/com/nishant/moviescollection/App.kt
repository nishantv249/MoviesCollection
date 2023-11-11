package com.nishant.moviescollection

import android.app.Application
import com.nishant.moviescollection.network.ApiService
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @set: Inject
    lateinit var apiService: ApiService



}