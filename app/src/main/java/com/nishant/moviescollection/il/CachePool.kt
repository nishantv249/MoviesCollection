package com.nishant.moviescollection.il

import android.graphics.Bitmap
import android.util.LruCache
import com.nishant.moviescollection.il.util.push

private const val MAX_SIZE = 8 * 1024*1024

private val lock = Any()
internal class CachePool (private val maxSize : Int = MAX_SIZE ) {

    private val pool = ArrayDeque<Bitmap>()

    private val cache = Cache(maxSize = MAX_SIZE,pool)

    fun get(url: String): Bitmap? {
        return cache.get(url)
    }

    fun put(url : String, bitmap: Bitmap){
        synchronized(lock) {
            cache.put(url, bitmap)
        }
    }

    fun getBitMap() : Bitmap?{
        return  pool.lastOrNull()
    }

}

private class Cache(val maxSize: Int, private val pool : ArrayDeque<Bitmap>) :
    LruCache<String,Bitmap>(maxSize){

    override fun entryRemoved(evicted: Boolean, key: String, oldValue: Bitmap, newValue: Bitmap) {
        synchronized(lock) {
            pool.push(oldValue)
        }
    }

}