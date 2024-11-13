package com.example.tradingapp

import android.app.Application
import com.example.tradingapp.model.repository.local.LocalUserCertificateGraph

class TradingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        LocalUserCertificateGraph.provide(this)
    }
}