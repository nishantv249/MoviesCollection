package com.nishant.moviescollection.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun TopBar(openDrawer : () -> Unit,onSearchClicked : ()->Unit ) {

    TopAppBar(title = {
        Text(text = "Home")
        }, navigationIcon = { Icon(imageVector = Icons.Filled.Menu, contentDescription = "menu", modifier = Modifier.clickable {
            openDrawer()
    })} , actions = {
            Icon(Icons.Filled.Search,contentDescription = "", modifier = Modifier.clickable {
                onSearchClicked()
            })
    })
}

@Composable
fun TopBarWithBack(title: String,onBackPressed : () -> Unit){
    TopAppBar(title = {
        Text(text = title)
    }, navigationIcon = {
        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back", modifier = Modifier.clickable{
            onBackPressed()
        })
    })
}



