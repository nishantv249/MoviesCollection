package com.nishant.il.il

import android.graphics.Bitmap
import android.util.LruCache
import com.nishant.il.il.util.push

private const val MAX_SIZE = 8 * 1024*1024

private val lock = Any()
internal class CachePool (private val maxSize : Int = MAX_SIZE ) {

    private val pool = ArrayDeque<Bitmap>()

    private val cache = Cache(maxSize = MAX_SIZE,pool)

    fun get(url: Request): Bitmap? {
        return cache.get(url)
    }

    fun put(url : Request, bitmap: Bitmap){
        synchronized(lock) {
            cache.put(url, bitmap)
        }
    }

    fun getBitMap() : Bitmap?{
        return  pool.lastOrNull()
    }

}

private class Cache(val maxSize: Int, private val pool : ArrayDeque<Bitmap>) :
    LruCache<Request,Bitmap>(maxSize){

    override fun entryRemoved(evicted: Boolean, key: Request, oldValue: Bitmap, newValue: Bitmap) {
        synchronized(lock) {
            pool.push(oldValue)
        }
    }

}