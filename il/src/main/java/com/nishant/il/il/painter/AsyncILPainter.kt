package com.nishant.il.il.painter

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
import com.nishant.il.il.ImageLoaderImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch


@Composable
fun rememberAsyncILPainter(url : String) : Painter{
    val painter = remember{
        AsyncILPainter(url)
    }
return painter
}

internal class AsyncILPainter(private val url: String) : Painter(),RememberObserver {

    private  var coroutineScope: CoroutineScope? = null
    private  var bitmap : Bitmap? by  mutableStateOf(null)

    override val intrinsicSize: Size
        get() =  Size.Unspecified

    private val sizeFlow = MutableStateFlow(Size(0.0f,0.0f))
    override fun DrawScope.onDraw() {
         sizeFlow.value = size
         if(bitmap != null){
             bitmap?.asImageBitmap()?.let { drawImage(it) }
         }
    }

    override fun onAbandoned() {
        coroutineScope?.cancel()
        coroutineScope = null
    }

    override fun onForgotten() {
        coroutineScope?.cancel()
        coroutineScope = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onRemembered() {
        if( coroutineScope != null){
            return
        }

        coroutineScope =  CoroutineScope(Dispatchers.Main.immediate)
        coroutineScope?.launch {
                sizeFlow.filter { it != Size(0.0f,0.0f) }.mapLatest{
                        ImageLoaderImpl.load(url, it.width.toInt(), it.height.toInt())
                }.catch {
                    coroutineScope?.cancel()
                    coroutineScope = null
                }.collect {
                bitmap = it
            }
        }
    }

}

private fun Int.toPixels(context : Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this.toFloat(), context.resources.displayMetrics).toInt()
}
