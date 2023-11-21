package com.nishant.feature.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.nishant.feature.ui.component.drawer.Drawer
import com.nishant.feature.ui.screens.BottomNavigationUI
import com.nishant.feature.ui.screens.bottom.top.TopRated
import kotlinx.coroutines.launch

@Composable
fun AppScaffold(navHostController: NavHostController,genreId : MutableState<String>, onSearch : () -> Unit,
                content : @Composable () ->Unit ){

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()


    Scaffold(scaffoldState = scaffoldState,
        topBar = { TopBar(openDrawer = {
            coroutineScope.launch {
                scaffoldState.drawerState.open()
            }
        }) {
            onSearch()
        }
        },
        bottomBar = {
            BottomNavigationUI(navHostController = navHostController , genreId = "" )
        },
        drawerContent = { Drawer( onGenreSelected = {
            genreId.value = it
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        }) }
    ) {
        Box(modifier = Modifier.padding(it)){
            content()
        }
    }
}