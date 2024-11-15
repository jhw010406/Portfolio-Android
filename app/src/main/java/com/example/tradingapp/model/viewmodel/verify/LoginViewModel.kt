package com.example.tradingapp.model.viewmodel.verify

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.model.data.user.UserCertificate
import com.example.tradingapp.model.data.user.UserInformation
import com.example.tradingapp.model.repository.local.LocalUserCertificateGraph
import com.example.tradingapp.model.repository.local.LocalUserCertificateRepository
import com.example.tradingapp.model.repository.UserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val localUserCertificateRepository : LocalUserCertificateRepository = LocalUserCertificateGraph.localUserCertificateRepository
) : ViewModel() {

    var myCertificate : UserCertificate? = null

    suspend fun doAutoLogin(tag : String) : Boolean {

        return withContext(Dispatchers.IO){
            myCertificate = localUserCertificateRepository.getUserCertificate()?.first()

            myCertificate?.let {
                Log.d(tag, "get user certification from local succeed")
                return@withContext it.keepLogin
            }

            return@withContext false
        }
    }

    fun Login(
        tag : String,
        id : String,
        password : String,
        keepLogin : Boolean,
        callback : (UserInformation?, Boolean) -> Unit
    ) {
        UserDataRepository.LoginUser(tag, id, password){ getUserCertificate, getUserInformation, isSuccessful ->

            if (isSuccessful) {
                myCertificate = getUserCertificate

                if (myCertificate != null) {
                    myCertificate!!.keepLogin = keepLogin
                    viewModelScope.launch {
                        localUserCertificateRepository.insert(myCertificate!!)
                    }
                    callback(getUserInformation, true)
                }
                else {
                    callback(null, false)
                }
            }
            else {
                callback(null, false)
            }
        }
    }

    fun Register(
        tag : String,
        id : String,
        password : String,
        callback : (UserCertificate?, Boolean) -> Unit
    ) {
        UserDataRepository.RegisterUser(tag, id, password){ getUserCertificate, isSuccessful ->

            if (isSuccessful) {
                myCertificate = getUserCertificate

                if (myCertificate != null) {
                    viewModelScope.launch {
                        localUserCertificateRepository.insert(myCertificate!!)
                    }
                    callback(getUserCertificate, true)
                }
                else {
                    callback(null, false)
                }
            }
            else {
                callback(null, false)
            }
        }
    }

    fun insert(userCertificate: UserCertificate){
        viewModelScope.launch(Dispatchers.IO){
            localUserCertificateRepository.insert(userCertificate)
        }
    }

    fun update(userCertificate: UserCertificate){
        viewModelScope.launch (Dispatchers.IO) {
            localUserCertificateRepository.update(userCertificate)
        }
    }

    fun deleteLocalData(){
        viewModelScope.launch (Dispatchers.IO) {
            localUserCertificateRepository.deleteAll()
        }
    }
}

fun reLogin(
    mainNavController : NavHostController
) {
    while (mainNavController.currentBackStackEntry != null) {
        mainNavController.popBackStack()
    }
    mainNavController.navigate(MainNavigationGraph.LOGIN)
}