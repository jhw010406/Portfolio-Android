package com.example.tradingapp.model.repository.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tradingapp.model.data.user.UserCertificate
import kotlinx.coroutines.flow.Flow

// dao = data access object, 데이터 접근을 위한 query 객체
@Dao
abstract class LocalUserCertificateDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(userCertificate : UserCertificate)

    @Update
    abstract suspend fun update(userCertificate: UserCertificate)

    @Query("update user_certificate set access_token = :accessToken")
    abstract suspend fun updateAccessToken(accessToken : String)

    @Query("update user_certificate set refresh_token = :refreshToken")
    abstract suspend fun updateRefreshToken(refreshToken : String)

    @Query("update user_certificate set keep_login = :keepLogin")
    abstract suspend fun updateKeepLogin(keepLogin : Boolean)

    @Query("delete from user_certificate")
    abstract suspend fun deleteAll()

    // Flow = 데이터 스트림, 연결된 데이터의 변화를 실시간으로 반영함.
    // 쿼리 수행으로 인해 받을 수 있는 DB의 값이 매번 달라질 수 있으므로,
    // 이를 실시간으로 반영하기 위해 Flow를 사용
    @Query("select * from user_certificate")
    abstract fun getUserCertificate() : Flow<UserCertificate>?

    @Query("select access_token from user_certificate")
    abstract fun getAccessToken() : Flow<String>?

    @Query("select refresh_token from user_certificate")
    abstract fun getRefreshToken() : Flow<String>?
}