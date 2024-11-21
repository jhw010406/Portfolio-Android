package com.example.tradingapp.utils.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getTextFieldColors(): TextFieldColors {
   return TextFieldDefaults.colors(
       focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
       unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
       focusedContainerColor = MaterialTheme.colorScheme.surface,
       unfocusedContainerColor = MaterialTheme.colorScheme.surface,
       focusedTextColor = MaterialTheme.colorScheme.onSurface,
       unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
       cursorColor = MaterialTheme.colorScheme.onSurface,
       unfocusedPrefixColor = Color(0xFF858C94),
       focusedPrefixColor = MaterialTheme.colorScheme.onSurface,
       selectionColors = TextSelectionColors(Color(0xFFFF6E1D), Color(0xC0FF9E4D))
   )
}