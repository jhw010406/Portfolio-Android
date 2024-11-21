package com.example.tradingapp.viewmodel.verify

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tradingapp.model.data.user.UserInformation
import com.example.tradingapp.model.repository.local.LocalUserCertificateRepository
import com.example.tradingapp.model.repository.local.LocalUserCertificateGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserInformationViewModel : ViewModel() {
    var userInfo : MutableState<UserInformation?> = mutableStateOf(null)
}

fun getRefreshTokenFromLocal(
    localUserCertificateRepository : LocalUserCertificateRepository = LocalUserCertificateGraph.localUserCertificateRepository,
    callback : (String?) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        localUserCertificateRepository.getRefreshToken()?.first().let {
            callback(it)
        }
    }
}