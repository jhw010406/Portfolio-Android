package com.example.tradingapp.model.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity("user_certificate")
data class UserCertificate(
    @PrimaryKey
    val id : String,

    @ColumnInfo("password")
    val password : String,

    @Expose(serialize = false, deserialize = false)
    @ColumnInfo("keep_login")
    var keepLogin : Boolean,

    //@Transient
    @ColumnInfo("access_token")
    var accessToken : String?,

    //@Transient
    @ColumnInfo("refresh_token")
    var refreshToken : String?
) {
    @Ignore
    var uid : Int = 0
}