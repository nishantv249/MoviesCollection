package com.nishant.moviescollection.il.engine

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.annotation.Px
import com.nishant.moviescollection.il.CachePool
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runInterruptible
import okhttp3.OkHttpClient
import okio.BufferedSource
import retrofit2.Retrofit
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException
import kotlin.coroutines.coroutineContext
import kotlin.math.roundToInt

internal class Engine(private val cachePool: CachePool) {

    //val apiService : ApiService = Retrofit.Builder().baseUrl(ApiURL.BASE_URL).client(OkHttpClient()).build().create(ApiService::class.java)

    suspend operator fun invoke(
        url: String,
        width: Int,
        height: Int,
        function: (bitMap: Bitmap?) -> Unit
    )  {
        val bitmap =
            runInterruptible(coroutineContext) {
                //getBitmap(url, cachePool.getBitMap(), width, height)
                null
            }

        if (bitmap != null) {
            //cachePool.put(url, bitmap = bitmap)
        }
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
/*
    private  fun getBitmap(
        url: String,
        reusableBitmap: Bitmap?,
        width: Int,
        height: Int,
    ) : Bitmap?  {
        var buffer : BufferedSource? = null
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.inMutable = true
            val bounds = apiService.getBytes(url).execute()
            buffer = bounds.body()?.source()
            BitmapFactory.decodeStream (buffer?.peek()?.inputStream(), null,  options)
            options.inSampleSize = calculateSampleSize(options, width, height)
            options.inJustDecodeBounds = false
            val bitmap = BitmapFactory.decodeStream (buffer?.peek()?.inputStream(), null,  options)
            if (bitmap != null && options.inSampleSize == 1 && (width > options.outWidth || height > options.outHeight)  ) {
                crop(bitmap,width,height,0.5f,0.5f)
            }else{
                bitmap
            }
        } catch ( e : Exception) {
            if( e is CancellationException){
                throw  e
            }
            if( e is InterruptedIOException && Thread.interrupted()){
                throw CancellationException("job cancelled ").initCause(e)
            }
            null
        }finally {
            try {
                buffer?.close()
            }catch (_ : Exception){

            }
        }
    }*/
}

private fun crop(
    src: Bitmap, w: Int, h: Int,
    horizontalCenterPercent: Float, verticalCenterPercent: Float
): Bitmap {
    val srcWidth = src.width
    val srcHeight = src.height
    if (w == srcWidth && h == srcHeight) {
        return src
    }
    val m = Matrix()
    val scale = (w.toFloat() / srcWidth).coerceAtLeast(h.toFloat() / srcHeight)
    m.setScale(scale, scale)
    var srcX: Int
    var srcY: Int
    val srcCroppedW: Int = (w / scale).roundToInt()
    val srcCroppedH: Int = (h / scale).roundToInt()
    srcX = (srcWidth * horizontalCenterPercent - srcCroppedW / 2).toInt()
    srcY = (srcHeight * verticalCenterPercent - srcCroppedH / 2).toInt()
    // Nudge srcX and srcY to be within the bounds of src
    srcX = srcX.coerceAtMost(srcWidth - srcCroppedW).coerceAtLeast(0)
    srcY = srcY.coerceAtMost(srcHeight - srcCroppedH).coerceAtLeast(0)
    return Bitmap.createBitmap(
        src, srcX, srcY, srcCroppedW, srcCroppedH, m,
        true
    )
}


private class Retry<T>(private var numOfRetries: Int = 3, val block: () -> T?) {

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
                timeout *= 2
                delay += timeout * 1000
                numOfRetries -= 1
                invoke()
            }
            null
        }
    }

}
