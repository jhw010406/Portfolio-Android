package com.example.tradingapp.view.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object LoadingBar{
    private val _isDisplayed = MutableStateFlow<Boolean>(false)
    val isDisplayed = _isDisplayed.asStateFlow()

    fun show(){
        _isDisplayed.value = true
    }

    fun hide(){
        _isDisplayed.value = false
    }
}

@Preview
@Composable
fun LoadingView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.onSurface,
            strokeWidth = 8.dp,
            strokeCap = StrokeCap.Round
        )
    }
}