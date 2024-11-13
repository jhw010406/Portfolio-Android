package com.example.tradingapp.model.repository.local

import com.example.tradingapp.model.data.user.UserCertificate
import kotlinx.coroutines.flow.Flow

class LocalUserCertificateRepository(private val localUserCertificateDAO: LocalUserCertificateDAO) {

    suspend fun insert(userCertificate: UserCertificate){
        localUserCertificateDAO.insert(userCertificate)
    }

    suspend fun update(userCertificate: UserCertificate){
        localUserCertificateDAO.update(userCertificate)
    }

    suspend fun updateKeepLogin(keepLogin : Boolean) = localUserCertificateDAO.updateKeepLogin(keepLogin)

    suspend fun updateAccessToken(accessToken : String) = localUserCertificateDAO.updateAccessToken(accessToken)

    suspend fun updateRefreshToken(refreshToken : String) = localUserCertificateDAO.updateRefreshToken(refreshToken)

    suspend fun deleteAll() = localUserCertificateDAO.deleteAll()

    fun getUserCertificate() : Flow<UserCertificate>? = localUserCertificateDAO.getUserCertificate()

    fun getAccessToken() : Flow<String>? = localUserCertificateDAO.getAccessToken()

    fun getRefreshToken() : Flow<String>? = localUserCertificateDAO.getRefreshToken()
}