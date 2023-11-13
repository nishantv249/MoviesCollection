package com.nishant.moviescollection.il

import android.graphics.Bitmap
import android.net.Uri
import android.os.Looper
import android.os.Message
import androidx.annotation.Px
import com.nishant.moviescollection.il.engine.Engine
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object ImageLoaderImpl : ImageLoader {

    private val ceh = CoroutineExceptionHandler { coroutineScope, throwable ->
        var entry: String? = null
        deferredMap.forEach() {
            if (it.value == coroutineScope.job) {
                entry = it.key
            }
        }
        deferredMap.remove(entry)
    }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + ceh)

    private val cache = CachePool()

    private val engine = Engine(cache)

    private val deferredMap = ConcurrentHashMap<String, Job>()

    private val handler = android.os.Handler(Looper.getMainLooper()) { message ->
        handleNewRequest(message.obj as Request)
        return@Handler true
    }

    private fun handleNewRequest(request: Request) {
        val job = coroutineScope.launch {
            engine(
                request.url,
                request.width,
                request.height,
                request.onBitmapFetched
            )
        }
        deferredMap[request.url] = job
    }

    override suspend fun load(url: String, @Px width: Int, @Px height: Int): Bitmap {
        return suspendCancellableCoroutine { cancellableContinuation ->
            if (cache.get(url) != null) {
                cancellableContinuation.resume(cache.get(url)!!)
            } else {
                val request = Request(url, height, width) {
                    if (it != null) {
                        deferredMap.remove(url)
                        cancellableContinuation.resume(it)
                    } else {
                        cancellableContinuation.resumeWithException(NullPointerException("null bitmap"))
                    }
                }
                val message = Message.obtain()
                message.what = request.hashCode()
                message.obj = request
                handler.sendMessageAtFrontOfQueue(message)

                cancellableContinuation.invokeOnCancellation {
                    handler.removeMessages(message.what)
                    deferredMap[url]?.cancel()
                }
            }
        }
    }

    override suspend fun load(uri: Uri): Bitmap {
        TODO("Not yet implemented")
    }

}
data class Request(
    val url: String,
    val height: Int,
    val width: Int,
    val onBitmapFetched: (bitMap: Bitmap?) -> Unit
)



