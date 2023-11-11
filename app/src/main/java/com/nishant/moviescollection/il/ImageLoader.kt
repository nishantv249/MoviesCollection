package com.nishant.moviescollection.il

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.Px
import kotlin.coroutines.CoroutineContext

interface ImageLoader {

     suspend fun load(url : String,@Px width: Int,@Px height: Int) : Bitmap

     suspend fun load(uri : Uri) : Bitmap

}