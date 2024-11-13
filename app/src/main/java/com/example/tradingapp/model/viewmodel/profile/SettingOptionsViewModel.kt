package com.example.tradingapp.model.viewmodel.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradingapp.model.repository.local.LocalUserCertificateRepository
import com.example.tradingapp.model.repository.local.LocalUserCertificateGraph
import kotlinx.coroutines.launch

class SettingOptionsViewModel (
    private val localUserCertificateRepository : LocalUserCertificateRepository = LocalUserCertificateGraph.localUserCertificateRepository
) : ViewModel() {

    fun updateKeepLogin(
        tag : String,
        keepLogin : Boolean
    ) {
        Log.d(tag, "updating keepLogin ...")
        viewModelScope.launch {
            localUserCertificateRepository.updateKeepLogin(keepLogin)
        }
        Log.d(tag, "update keepLogin succeed!")
    }
}