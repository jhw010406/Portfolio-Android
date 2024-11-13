package com.example.tradingapp.model.repository.local

import android.content.Context
import androidx.room.Room

object LocalUserCertificateGraph {

    lateinit var database : LocalUserCertificateDB

    val localUserCertificateRepository by lazy {
        LocalUserCertificateRepository(database.localUserCertificateDAO())
    }

    fun provide(context : Context){
        database = Room.databaseBuilder(context, LocalUserCertificateDB::class.java, "myCertificate.db").build()
    }
}