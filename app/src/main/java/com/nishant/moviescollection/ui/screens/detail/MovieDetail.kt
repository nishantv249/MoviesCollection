package com.nishant.moviescollection.ui.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.nishant.moviescollection.network.ApiURL
import com.nishant.moviescollection.network.Result
import com.nishant.moviescollection.network.models.Cast
import com.nishant.moviescollection.network.models.MovieDetail
import com.nishant.moviescollection.network.models.MovieItem
import com.nishant.moviescollection.ui.LoadingContent
import com.nishant.moviescollection.ui.component.ExpandingText
import com.nishant.moviescollection.ui.component.TopBarWithBack
import com.nishant.moviescollection.ui.component.text.SubtitlePrimary
import com.nishant.moviescollection.ui.component.text.SubtitleSecondary

@Composable
fun MovieDetail(
    movieDetailViewModel: MovieDetailViewModel = hiltViewModel(),
    movieId: Int,
    onBackPressed: () -> Unit,
    onArtistClicked: (id: Int) -> Unit
) {

    LaunchedEffect(Unit) {
        movieDetailViewModel.getMovieDetail(movieId)
        movieDetailViewModel.getRecommendedMovies(movieId)
        movieDetailViewModel.getCredits(movieId)
    }

    val movieDetail by movieDetailViewModel.movieDetail.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarWithBack(title = if(movieDetail is Result.Success){
            (movieDetail as Result.Success).t.movieDetail.title
        }else{
            ""
        }) {
            onBackPressed()
        }

        LoadingContent(state = movieDetail) { movieDetailResponse ->
            val movieDetailI = movieDetailResponse.movieDetail
            Column(modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)) {
                val thumb = movieDetailI.backdrop_path
                val painter = rememberAsyncImagePainter(model = ApiURL.IMAGE_URL.plus(thumb))
                Image(
                    painter = painter, contentDescription = "", modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = movieDetailI.title, modifier = Modifier.padding(16.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.W700,
                    maxLines = 1
                )

                MovieDetailSecondary(movieDetailResponse.movieDetail)

                Spacer(modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth())
                
               ExpandingText(text = movieDetailI.overview, modifier = Modifier.padding(start = 16.dp, end = 16.dp))

                if(movieDetailResponse.movieList.isNotEmpty()) {
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth()
                    )

                    SimilarMovies(movieDetailResponse.movieList)
                }

                Spacer(modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth())

                CastList(movieDetailResponse.cast,onArtistClicked)
            }
        }
    }
}

@Composable
fun MovieDetailSecondary(movieDetailI: MovieDetail) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        Column(Modifier.weight(1f)) {
            SubtitlePrimary(text = movieDetailI.original_language)
            SubtitleSecondary(text = "Language")
        }

        Column(Modifier.weight(1f)) {
            SubtitlePrimary(text = movieDetailI.vote_average.toString())
            SubtitleSecondary(text = "Rating")
        }
        Column(Modifier.weight(1f)) {
            SubtitlePrimary(
                text = movieDetailI.runtime.toString()
            )
            SubtitleSecondary(
                text = "Duration"
            )
        }
        Column(Modifier.weight(1f)) {
            SubtitlePrimary(
                text = movieDetailI.release_date
            )
            SubtitleSecondary(
                text = "Release Date"
            )
        }
    }
}

@Composable
fun SimilarMovies(movieList: List<MovieItem>) {
    Text(text = "Similar Movies", fontSize = 17.sp, fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 16.dp))

    Spacer(modifier = Modifier
        .height(8.dp)
        .fillMaxWidth())

    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier
        .padding(start = 16.dp)
    ){
        items(movieList){
            SimilarMovieItem(movieItem = it)
        }
    }
}

@Composable
fun CastList(cast: List<Cast>, onArtistClicked: (id: Int) -> Unit) {

    Text(text = "Cast", fontSize = 17.sp, fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 16.dp))

    Spacer(modifier = Modifier
        .height(8.dp)
        .fillMaxWidth())

    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier
        .padding(start = 16.dp)
    ){
        items(cast){
            Cast(it,onArtistClicked)
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun Cast(cast: Cast, onArtistClicked : (id : Int) -> Unit) {
    val painter =
        rememberAsyncImagePainter(model = ApiURL.IMAGE_URL.plus(cast.profilePath))
    Image(
        painter = painter, contentDescription = "",
        Modifier
            .size(80.dp)
            .clip(CircleShape).clickable {
                onArtistClicked(cast.id)
            },
        contentScale = ContentScale.Crop
    )
}

@Composable
fun SimilarMovieItem(movieItem: MovieItem) {
    val painter =
        rememberAsyncImagePainter(model = ApiURL.IMAGE_URL.plus(movieItem.posterPath))
    Image(
        painter = painter, contentDescription = "",
        Modifier
            .width(120.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
    )
}

