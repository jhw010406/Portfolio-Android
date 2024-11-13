package com.example.tradingapp.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ChatView(
    tag : String
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212123))
    ) {
        Text(text = "chat")
    }
}