package com.nishant.moviescollection.il.engine

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.Px
import com.nishant.moviescollection.il.CachePool
import com.nishant.moviescollection.network.ApiService
import com.nishant.moviescollection.network.ApiURL
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runInterruptible
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException
import kotlin.coroutines.coroutineContext

internal class Engine(private val cachePool: CachePool) {

    val apiService : ApiService = Retrofit.Builder().baseUrl(ApiURL.BASE_URL).client(OkHttpClient()).build().create(ApiService::class.java)

    suspend operator fun invoke(
        url: String,
        width: Int,
        height: Int,
        function: (bitMap: Bitmap?) -> Unit
    )  {
        val bitmap = Retry{
            runInterruptible(coroutineContext) {
                getBitmap(url,null,width,height)
            }
        }()
        function.invoke(bitmap)
    }

    private fun calculateSampleSize(
        options: BitmapFactory.Options,
        @Px reqWidth: Int,
        @Px reqHeight: Int
    ): Int {
        val (width: Int, height: Int) = options.run { outWidth to outHeight }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private  fun getBitmap(
        url: String,
        reusableBitmap: Bitmap?,
        width: Int,
        height: Int,
    ) : Bitmap?  {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            val bounds = apiService.getBytes(url).execute()
            BitmapFactory.decodeStream(bounds.body()?.byteStream(), null, options)
            options.inSampleSize = calculateSampleSize(options, width, height)
            options.inJustDecodeBounds = false
            val decodeStream = apiService.getBytes(url).execute()
            BitmapFactory.decodeStream(decodeStream.body()?.byteStream(), null, options)
        } catch ( e : Exception) {
            println(" $url exception $e ")
            null
        }
    }

}

private class Retry<T>(private var numOfRetries: Int = 3, val block: suspend () -> T?) {

    private var timeout = 1
    private var delay: Long = 0

    suspend operator fun invoke(): T? {
        delay(delay)
        return try {
            block()
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            if ((e is TimeoutException || e is SocketTimeoutException) && numOfRetries > 0) {
                println("retrying $numOfRetries")
                timeout *= 2
                delay += timeout * 1000
                numOfRetries -= 1
                invoke()
            }
            null
        }
    }

}