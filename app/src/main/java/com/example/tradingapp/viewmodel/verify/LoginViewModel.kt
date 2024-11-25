package com.example.tradingapp.viewmodel.verify

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
import com.example.tradingapp.view.other.RootSnackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val localUserCertificateRepository : LocalUserCertificateRepository = LocalUserCertificateGraph.localUserCertificateRepository
) : ViewModel() {

    var tryAutoLogin = false
    var myCertificate: UserCertificate? = null

    suspend fun doAutoLogin(tag : String = "test") : Boolean {

        return withContext(Dispatchers.IO){
            myCertificate = localUserCertificateRepository.getUserCertificate()?.first()

            myCertificate?.let {
                Log.d(tag, "get user certification from local succeed")
                return@withContext it.keepLogin
            }

            return@withContext false
        }
    }

    fun login(
        tag : String,
        id : String,
        password : String,
        keepLogin : Boolean,
        callback : (UserInformation?, Boolean) -> Unit
    ) {
        UserDataRepository.loginUser(tag, id, password){ getUserCertificate, getUserInformation, responseCode ->

            when (responseCode / 100) {
                2 -> {
                    myCertificate = getUserCertificate
                    myCertificate!!.keepLogin = keepLogin
                    viewModelScope.launch {
                        localUserCertificateRepository.insert(myCertificate!!)
                    }
                    RootSnackbar.show("로그인 성공")
                    callback(getUserInformation, true)
                }
                4 -> {
                    RootSnackbar.show("올바르지 않은 계정입니다.")
                    callback(null, false)
                }
                else -> {
                    RootSnackbar.show("서버 연결 실패.\n다시 시도해주세요.")
                    callback(null, false)
                }
            }
        }
    }

    fun Register(
        tag : String,
        id : String,
        password : String,
        callback : (UserInformation?, Boolean) -> Unit
    ) {
        UserDataRepository.RegisterUser(
            tag,
            UserCertificate(id, password, false, null, null)
        ){ getUserCertificate, getUserInformation, responseCode ->

            when (responseCode / 100) {
                2 -> {
                    myCertificate = getUserCertificate

                    viewModelScope.launch {
                        localUserCertificateRepository.insert(getUserCertificate)
                    }
                    callback(getUserInformation, true)
                }
                4 -> {
                    RootSnackbar.show("이미 존재하는 계정입니다.")
                    callback(null, false)
                }
                else -> {
                    RootSnackbar.show("서버 연결 실패.\n다시 시도해주세요.")
                    callback(null, false)
                }
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