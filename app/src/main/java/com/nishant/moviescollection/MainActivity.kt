package com.nishant.moviescollection

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import com.nishant.moviescollection.il.ImageLoaderImpl
import com.nishant.moviescollection.il.painter.rememberAsyncILPainter
import com.nishant.moviescollection.ui.NavGraph
import com.nishant.moviescollection.ui.theme.MoviesdupTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread
import kotlin.coroutines.resume


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MoviesdupTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    NavGraph(genreId = "")
                    //showImage()
                }
            }
        }
    }

    @Composable
    private fun showImage() {
        //rememberAsyncImagePainter(model = )

        val painter = rememberAsyncILPainter(url = "https://img.freepik.com/free-vector/diwali-festival-patterned-background_53876-118874.jpg?w=360&t=st=1699424296~exp=1699424896~hmac=d05b7266dc402b48ebd4f175c2f4c83b7382974b92c058f2ecac8530d74a03b3" )

        Image(painter = painter, contentDescription = "",
            modifier = Modifier.size(120.dp))
    }

}

