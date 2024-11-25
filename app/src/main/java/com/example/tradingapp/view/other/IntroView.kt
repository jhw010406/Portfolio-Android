package com.example.tradingapp.view.other

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.tradingapp.R

@Preview
@Composable
fun IntroView(){
    val currentContext = LocalContext.current
    var screenMaxWidth by rememberSaveable { mutableIntStateOf(0) }
    var animationStep by rememberSaveable { mutableIntStateOf(0) }
    val logoBodyRotationZ by animateFloatAsState(targetValue =
        if (animationStep > 0) { 0f }
        else { 540f },
        animationSpec = tween(durationMillis = 1000)
    )
    val logoBodySize by animateFloatAsState(targetValue =
        if (animationStep > 0) { 0.5f }
        else { 2f },
        animationSpec = tween(durationMillis = 600)
    )
    val moveLogoBodyOffsetX by animateIntAsState(targetValue =
        if (animationStep > 0) { 0 }
        else { 540 },
        animationSpec = tween(durationMillis = 1000)
    )
    val logoCenterSize by animateFloatAsState(targetValue =
        if (animationStep > 1) { 0.5f }
        else { 0f },
        animationSpec = spring(Spring.DampingRatioMediumBouncy)
    )
    val logoHeadSize by animateFloatAsState(targetValue =
        if (animationStep > 2) { 0.5f }
        else { 0f },
        animationSpec = spring(0.35f)
    )

    BackHandler {
        (currentContext as Activity).finish()
    }

    LaunchedEffect(screenMaxWidth) {

        if (screenMaxWidth > 0) {
            if (animationStep < 1) { animationStep = 1 }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { screenMaxWidth = it.width },
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.logo_head),
            contentDescription = "intro logo head",
            modifier = Modifier
                .offset(y = (-30).dp)
                .graphicsLayer {
                    scaleX = logoHeadSize
                    scaleY = logoHeadSize
                }
        )

        Image(
            painter = painterResource(id = R.drawable.logo_body),
            contentDescription = "intro logo body",
            modifier = Modifier
                .graphicsLayer {
                    scaleX = logoBodySize
                    scaleY = logoBodySize
                    rotationZ = logoBodyRotationZ
                    if (animationStep < 2 && logoBodyRotationZ < 5f) {
                        animationStep = 2
                    }
                }
                .offset { IntOffset(moveLogoBodyOffsetX, 0) }
        )

        Image(
            painter = painterResource(id = R.drawable.logo_center),
            contentDescription = "intro logo center",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .graphicsLayer {
                    scaleX = logoCenterSize
                    scaleY = logoCenterSize
                    if (logoCenterSize > 0.475f) { animationStep = 3 }
                }
        )
    }
}