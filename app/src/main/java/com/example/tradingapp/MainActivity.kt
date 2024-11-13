package com.example.tradingapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.tradingapp.utils.ui.theme.TradingAppTheme
import com.example.tradingapp.view.graph.MainNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TradingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            // 시스템 ui로 인해 white space가 생기지 않도록 함.
                            .consumeWindowInsets(innerPadding),
                        color = Color(0xFF212123)
                    ){
                        MainNavGraph()
                    }
                }
            }
        }
    }
}