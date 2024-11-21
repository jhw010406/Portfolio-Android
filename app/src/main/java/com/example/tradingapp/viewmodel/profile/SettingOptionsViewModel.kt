package com.example.tradingapp.viewmodel.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradingapp.model.repository.local.LocalUserCertificateRepository
import com.example.tradingapp.model.repository.local.LocalUserCertificateGraph
import kotlinx.coroutines.launch

class SettingOptionsViewModel (
    private val localUserCertificateRepository : LocalUserCertificateRepository = LocalUserCertificateGraph.localUserCertificateRepository
) : ViewModel() {
    var backStackEntryId = mutableStateOf<String?>(null)

    fun updateKeepLogin(
        tag : String,
        keepLogin : Boolean
    ) {
        viewModelScope.launch {
            localUserCertificateRepository.updateKeepLogin(keepLogin)
            Log.d(tag, "update keep login to ${keepLogin}")
        }
    }
}