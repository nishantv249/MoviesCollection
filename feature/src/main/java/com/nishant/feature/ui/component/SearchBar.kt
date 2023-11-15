package com.nishant.feature.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import com.nishant.moviescollection.ui.theme.Purple500

@Composable
fun SearchBar(onBackPressed : () ->Unit,onSearch : (query : String) -> Unit) {

    var text by remember {
        mutableStateOf("")
    }

    val focusRequester = remember {
        FocusRequester()
    }

    TextField(value = text, onValueChange = {
        text = it
        onSearch(text)
    }, leadingIcon = {
        Icon(Icons.Filled.ArrowBack, contentDescription = "", modifier = Modifier.clickable {
            onBackPressed()
        }, tint = Color.White)
    } , trailingIcon = {
        if(text.isNotEmpty()){
            Icon(imageVector = Icons.Filled.Clear, contentDescription = "clear", modifier = Modifier.clickable {
                text = ""
                onSearch(text)
            }, tint = Color.White)
        }else{
            Icon(imageVector = Icons.Filled.Search, contentDescription = "", tint = Color.White)
        }
    }, modifier = Modifier
        .fillMaxWidth()
        .focusRequester(focusRequester)
        .background(color = Purple500),
        colors = TextFieldDefaults.textFieldColors(cursorColor = Color.White)
    )

    LaunchedEffect(key1 = Unit ){
        focusRequester.requestFocus()
    }

}