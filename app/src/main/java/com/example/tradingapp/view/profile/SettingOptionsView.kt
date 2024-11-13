package com.example.tradingapp.view.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tradingapp.R
import com.example.tradingapp.model.data.user.UserCertificate
import com.example.tradingapp.model.viewmodel.profile.SettingOptionsViewModel

@Composable
fun SettingOptionsView (
    tag : String,
    userCertificate: UserCertificate,
    settingOptionsViewModel: SettingOptionsViewModel = viewModel(),
    mainNavController: NavHostController
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212123))
    ) {
        SettingOptionsViewHeader(mainNavController)

        SettingOptionsViewBody(tag, userCertificate, settingOptionsViewModel)
    }
}

@Composable
fun SettingOptionsViewHeader (
    mainNavController: NavHostController
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF212123))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
            tint = Color.White,
            modifier = Modifier.clickable { mainNavController.popBackStack() },
            contentDescription = "back"
        )
    }
    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF636365)
    )
}

@Composable
fun SettingOptionsViewBody (
    tag : String,
    userCertificate: UserCertificate,
    settingOptionsViewModel: SettingOptionsViewModel
) {
    var keepLogin by rememberSaveable {
        mutableStateOf(userCertificate.keepLogin)
    }

    LazyColumn (

    ) {
        item { 
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "자동 로그인",
                    fontSize = 20.sp,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    color = Color.White)
                Checkbox(
                    checked = keepLogin,
                    modifier = Modifier.size(24.dp),
                    onCheckedChange = {
                        keepLogin = !keepLogin
                        userCertificate.keepLogin = keepLogin
                        settingOptionsViewModel.updateKeepLogin(tag, keepLogin)
                    }
                )
            }
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF636365)
            )
        }
    }
}