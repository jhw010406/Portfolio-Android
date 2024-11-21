package com.example.tradingapp.model.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tradingapp.model.data.user.UserCertificate

@Database(entities = [UserCertificate::class], version = 1, exportSchema = false)
abstract class LocalUserCertificateDB : RoomDatabase() {

    abstract fun localUserCertificateDAO() : LocalUserCertificateDAO
}