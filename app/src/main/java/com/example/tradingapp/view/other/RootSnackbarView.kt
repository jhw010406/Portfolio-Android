package com.example.tradingapp.view.other

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object RootSnackbar{
    val snackbarHostState = SnackbarHostState()
    var currentSnackbar : SnackbarData? = null
    var message : String = ""
    const val duration : Long = 2000L

    fun show(inputMessage : String) {
        this.hide()
        message = inputMessage
        CoroutineScope(Dispatchers.Default).launch {
            // showSnackbar를 통해 snackbar data가 생성되고, snackbarHostState에 담긴다.
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Indefinite)
        }
    }

    fun hide() {
        currentSnackbar?.dismiss()
        currentSnackbar = null
    }
}

@Composable
fun RootSnackbarView(
    snackbarData: SnackbarData
) {
    var pressedSnackbar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(RootSnackbar.duration)
        RootSnackbar.hide()
    }

    LaunchedEffect(pressedSnackbar) {

        if (pressedSnackbar) {
            RootSnackbar.hide()
        }
    }

    Surface(
        modifier = Modifier
            .wrapContentSize()
            .clickable { pressedSnackbar = true },
        color = MaterialTheme.colorScheme.outline,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = snackbarData.visuals.message,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
    }
}