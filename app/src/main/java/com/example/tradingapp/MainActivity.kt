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
import com.example.tradingapp.utils.ui.theme.TradingAppTheme
import com.example.tradingapp.view.graph.MainNavGraph
import com.example.tradingapp.view.other.RootSnackbar
import com.example.tradingapp.view.other.RootSnackbarView


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
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