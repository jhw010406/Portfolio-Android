package com.example.tradingapp.model.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tradingapp.model.data.user.UserCertificate
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [UserCertificate::class], version = 1, exportSchema = false)
abstract class LocalUserCertificateDB : RoomDatabase() {

    abstract fun localUserCertificateDAO() : LocalUserCertificateDAO
}