package com.example.tradingapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tradingapp.utils.ui.theme.TradingAppTheme
import com.example.tradingapp.view.graph.MainNavGraph
import com.example.tradingapp.view.other.RootSnackbar
import com.example.tradingapp.view.other.RootSnackbarView
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.Instant


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        // onCreate가 수행되자마자 splash screen 종료 신호를 내보낸다.
        splashScreen.setKeepOnScreenCondition{ false }

        splashScreen.setOnExitAnimationListener{ splashScreenView ->
            val animationDuration = splashScreenView.iconAnimationDurationMillis
            val animationStart = splashScreenView.iconAnimationStartMillis
            val remainingDuration = animationDuration - (
                        (Instant.now().toEpochMilli() - animationStart).let { if (it >= 0) { it } else { 0 } }
                    )

            // splash screen duration이 만료될 때까지만 delay
            runBlocking { delay(remainingDuration) }

            // splash screen 종료
            splashScreenView.remove()
        }

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TradingAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(
                            modifier = Modifier.offset(y = (-60).dp),
                            hostState = RootSnackbar.snackbarHostState
                        ) { snackbarData ->
                            RootSnackbar.currentSnackbar = snackbarData
                            RootSnackbarView(snackbarData = snackbarData)
                        }
                    }
                ) { innerPadding ->
                    Box (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            // 시스템 ui로 인해 white space가 생기지 않도록 함.
                            .consumeWindowInsets(innerPadding)
                    ){
                        MainNavGraph()
                    }
                }
            }
        }
    }
}