package com.nishant.moviescollection.il.painter

import android.content.Context
import android.graphics.Bitmap
import android.util.TypedValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import com.nishant.moviescollection.il.ImageLoaderImpl
import com.nishant.moviescollection.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
//import kotlinx.coroutines.flow.internal.NopCollector.emit
import kotlinx.coroutines.launch
import javax.inject.Inject


@Composable
fun rememberAsyncILPainter(url : String) : Painter{
    val context = LocalContext.current
    val painter = remember{
        AsyncILPainter(url, context)
    }
return painter
}

internal class AsyncILPainter(private val url: String,private val current: Context) : Painter(),RememberObserver {


    private  var scop: CoroutineScope? = null
    private  var bitmap : Bitmap? by  mutableStateOf(null)
    override val intrinsicSize: Size
        get() = if(bitmap != null) bitmap?.height?.toFloat()
            ?.let { bitmap?.width?.toFloat()?.let { it1 -> Size(it1, it) } }!! else Size.Unspecified

    private val sizeFlow = MutableStateFlow(Size(0.0f,0.0f))
    override fun DrawScope.onDraw() {
         sizeFlow.value = size
         if(bitmap != null){
             bitmap?.asImageBitmap()?.let { drawImage(it) }
         }
    }

    override fun onAbandoned() {
        println("abandoned ..cancelling")
        //scop?.cancel()
        //scop = null
    }

    override fun onForgotten() {
        scop?.launch {
            sizeFlow.filter { it != Size(0.0f, 0.0f) }.mapLatest {
                    ImageLoaderImpl.cancel(url)
            }.collect{

            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onRemembered() {
        if( scop != null){
            return
        }

        scop =  CoroutineScope(Dispatchers.Main.immediate)
        scop?.launch {
                sizeFlow.filter { it != Size(0.0f,0.0f) }.mapLatest{
                        ImageLoaderImpl.load(url, it.width.toInt(), it.height.toInt())
                }.catch {
                    scop?.cancel()
                    scop = null
                }.collect {
                    println("bitmap collected")
                bitmap = it
            }
        }
    }

}

private fun Int.toPixels(context : Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this.toFloat(), context.resources.displayMetrics).toInt()
}
