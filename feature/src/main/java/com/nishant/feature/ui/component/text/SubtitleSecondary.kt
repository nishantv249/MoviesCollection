package com.nishant.feature.ui.component.text

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun SubtitleSecondary(text : String) {
    Text(text = text, style = MaterialTheme.typography.subtitle2)
}