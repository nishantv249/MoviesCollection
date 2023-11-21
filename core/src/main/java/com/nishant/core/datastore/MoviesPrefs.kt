package com.nishant.core.datastore

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class MoviesPrefs @Inject constructor(context : Context) {

    private val sharedPrefs = context.getSharedPreferences("movies_prefs",
        Context.MODE_PRIVATE)

    private val NOW_PLAYING_ITEM_PAGE_COUNT = "now_playing_item_page_count"

    var count = 0
    fun setNowPlayingItemPageCount(count : Int){
        this.count = count
        //sharedPrefs.edit().putInt(NOW_PLAYING_ITEM_PAGE_COUNT, count).apply()
    }

    fun getNowPlayingItemPageCount() : Int{
        return count
    }

}

