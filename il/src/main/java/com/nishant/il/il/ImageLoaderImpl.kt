package com.nishant.il.il

import android.graphics.Bitmap
import android.net.Uri
import android.os.Looper
import android.os.Message
import androidx.annotation.Px
import com.nishant.il.il.engine.Engine
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

    private val ceh = CoroutineExceptionHandler { coroutineScope, _ ->
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
            engine(request)
        }
        deferredMap[request.url] = job
    }

    override suspend fun load(url: String, @Px width: Int, @Px height: Int): Bitmap {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val request = Request(url, height, width) {
                if (it != null) {
                    deferredMap.remove(url)
                    cancellableContinuation.resume(it)
                } else {
                    cancellableContinuation.resumeWithException(NullPointerException("null bitmap"))
                }
            }
            if (cache.get(request) != null) {
                cancellableContinuation.resume(cache.get(request)!!)
            } else {
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
){
    override fun equals(other: Any?): Boolean {
        val old = other as Request
        return width == old.width && height == old.height && url == old.url
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + height
        result = 31 * result + width
        return result
    }

}



