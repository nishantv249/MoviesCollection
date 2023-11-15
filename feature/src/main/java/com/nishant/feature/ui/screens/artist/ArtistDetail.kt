package com.nishant.feature.ui.screens.artist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.nishant.core.network.api.MoviesApiService
import com.nishant.core.network.models.ArtistDetailDto
import com.nishant.core.repo.LoadingState
import com.nishant.feature.ui.LoadingContent
import com.nishant.feature.ui.component.TopBarWithBack

@Composable
fun ArtistDetail(artistId : Int, artistDetailViewModel: ArtistDetailViewModel = hiltViewModel(),
                 onBackPressed : () -> Unit) {

    val artistDetail by artistDetailViewModel.artistDetailFlow.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit){
        artistDetailViewModel.getArtistDetail(artistId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        (if(artistDetail is LoadingState.Success){
            (artistDetail as LoadingState.Success).t?.name
        }else{
            "Artist Detail"
        })?.let {
            TopBarWithBack(title = it) {
                onBackPressed()
            }
        }

        LoadingContent(state = artistDetail) { artistDetail ->
            if(artistDetail != null) {
                ArtistProfile(artistDetail)
            }
        }

    }

}

@Composable
fun ArtistProfile(artistDetail: ArtistDetailDto) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())) {
        Row {
            val painter = rememberAsyncImagePainter(model = MoviesApiService.IMAGE_URL.plus(artistDetail.profilePath))
            Image(painter = painter, contentDescription = "", modifier = Modifier
                .width(190.dp)
                .height(250.dp)
                .clip(
                    RoundedCornerShape(8.dp)
                ), contentScale = ContentScale.Crop)

           Column {
               Text(
                   modifier = Modifier.padding(start = 8.dp),
                   text = artistDetail.name,
                   fontSize = 26.sp,
                   fontWeight = FontWeight.Medium
               )
               PersonalInfo(title = "Known for" , info = artistDetail.knownForDepartment)
               PersonalInfo(title = "Gender", info =artistDetail.gender.genderAsString() )
               artistDetail.birthday?.let { PersonalInfo(title = "Birthday", info = it) }
               artistDetail.placeOfBirth?.let { PersonalInfo(title = "Place of birth", info = it) }
           }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = artistDetail.biography, style = MaterialTheme.typography.body1)
    }
}

private fun Int.genderAsString(): String {
    return when (this) {
        1 -> "Female"
        2 -> "Male"
        else -> ""
    }
}

@Composable
fun PersonalInfo(title: String, info: String) {
    Column(modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = info, fontSize = 16.sp
        )
    }
}


