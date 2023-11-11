package com.nishant.moviescollection.il

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.Px
import com.nishant.moviescollection.il.engine.Engine
import com.nishant.moviescollection.network.ApiService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.android.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.logging.Handler
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object ImageLoaderImpl : ImageLoader  {


    private  var job: Job? = null

    private val coroutineScope = CoroutineScope(SupervisorJob()+ Dispatchers.IO)

    private val cache  = CachePool()

    private val engine = Engine(cache)

    private val deferredMap = ConcurrentHashMap<String,Job>()

    private val requestQueue = ConcurrentLinkedDeque<Request>()

    private fun worker() {
        job = coroutineScope.launch {
            supervisorScope {
                while (true) {
                    if(requestQueue.isEmpty()){
                        continue
                    }

                    val request = requestQueue.removeFirst()
                    try {
                       val job =  launch {
                            delay(100)
                           engine(request.url, request.width, request.height, request.onBitmapFetched) }
                        deferredMap[request.url] = job
                        println(" ${request.url} in queue ")
                    } catch (e: Exception) {
                        if (e is CancellationException) {
                            println("cancellation exception")
                            throw e
                        }
                    }
                }
            }
        }
    }

    override suspend fun load(url: String,@Px width : Int,@Px height : Int): Bitmap {
        return suspendCancellableCoroutine { cancellableContinuation ->
            if(cache.get(url) != null){
                cancellableContinuation.resume(cache.get(url)!!)
            }else {
                println("request added $url")
                requestQueue.addFirst(Request(url,height,width) {
                    if (it != null) {
                        cache.put(url, it)
                        deferredMap.remove(url)
                        cancellableContinuation.resume(it)
                    } else {
                        cancellableContinuation.resumeWithException(NullPointerException("null bitmap"))
                    }
                })

                if (job == null ||  !job?.isActive!!) {
                    worker()
                }
            }

        }
    }

    override suspend fun load(uri: Uri): Bitmap {
        TODO("Not yet implemented")
    }

    suspend fun  cancel(url: String) {
        suspendCancellableCoroutine<Unit> { continuation ->
            if (deferredMap[url] == null) {
                println(" $url not found in queue")
                var req: Request? = null
                requestQueue.forEach {
                    if (it.url == url) {
                        req = it
                    }
                }
                if (req != null) {
                    requestQueue.remove(req)
                    requestQueue.addLast(req)
                }
                continuation.resume(Unit)
            }else {
                println("cancelling url $url")
                val deferred = deferredMap[url]
                deferred?.cancel()
                deferredMap.remove(url)
                var req: Request? = null
                requestQueue.forEach {
                    if (it.url == url) {
                        req = it
                    }
                }
                if (req != null) {
                    requestQueue.remove(req)
                    requestQueue.addLast(req)
                }
                continuation.resume(Unit)
            }
        }
    }

}

data class Request(val url : String,val height: Int,val width : Int,val onBitmapFetched : ( bitMap : Bitmap?) -> Unit)



