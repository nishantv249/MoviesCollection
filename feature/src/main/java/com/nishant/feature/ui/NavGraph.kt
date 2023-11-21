package com.nishant.feature.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nishant.feature.ui.navigation.Screen
import com.nishant.feature.ui.screens.MainScreen
import com.nishant.feature.ui.screens.artist.ArtistDetail
import com.nishant.feature.ui.screens.bottom.popular.PopularRoute
import com.nishant.feature.ui.screens.bottom.top.TopRated
import com.nishant.feature.ui.screens.bottom.top.TopRatedRoute
import com.nishant.feature.ui.screens.bottom.upcoming.UpcomingRoute
import com.nishant.feature.ui.screens.detail.MovieDetail
import com.nishant.feature.ui.screens.search.SearchScreen

@Composable
fun NavGraph(genreId: String) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Home" ){
        composable(Screen.Home.route){
            MainScreen(navController){
                navController.navigate("detail".plus("/").plus(it))
            }
        }

        composable(Screen.Popular.route){
            PopularRoute(navController, onSearch = {
                navController.navigate("search")
            }){
                navController.navigate("detail".plus("/").plus(it))
            }
        }

        composable(Screen.TopRated.route){
            TopRatedRoute(navController, onSearch = {
                navController.navigate("search")
            }){
                navController.navigate("detail".plus("/").plus(it))
            }
        }

        composable(Screen.Upcoming.route){
            UpcomingRoute(navController, onSearch = {
                navController.navigate("search")
            }){
                navController.navigate("detail".plus("/").plus(it))
            }
        }

        composable("search"){
            SearchScreen(onMovieClicked = {
                navController.navigate("detail".plus("/").plus(it))
            }) {
                navController.popBackStack()
            }
        }

        composable(Screen.MovieDetail.route.plus("/{")
            .plus(Screen.MovieDetail.objectName).plus("}"), arguments = listOf(
            navArgument(Screen.MovieDetail.objectName){
                type = NavType.IntType
            })){ it ->
            it.arguments?.getInt(Screen.MovieDetail.objectName)
                ?.let {
                        it1 -> MovieDetail(movieId = it1,
                    onBackPressed = {navController.popBackStack()}){ id ->
                        navController.navigate(Screen.ArtistDetail.route.plus("/").plus(id))
                } }
        }

        composable(Screen.ArtistDetail.route.plus("/{").plus(Screen.ArtistDetail.objectName).plus("}"), arguments = listOf(
            navArgument(Screen.ArtistDetail.objectName){
                type = NavType.IntType
            }
        )){
            it.arguments?.getInt(Screen.ArtistDetail.objectName)
                ?.let {
                        it1 -> ArtistDetail(it1){
                            navController.popBackStack()
                }}
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?.substringBeforeLast("/")
}


