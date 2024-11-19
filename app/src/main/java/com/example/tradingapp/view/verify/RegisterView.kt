package com.example.tradingapp.view.verify

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tradingapp.model.data.navigation.HomeNavigationGraph
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.model.data.user.UserInformation
import com.example.tradingapp.model.viewmodel.verify.LoginViewModel
import com.example.tradingapp.model.viewmodel.verify.UserInformationViewModel

@Composable
fun RegisterView(
    tag : String,
    loginViewModel: LoginViewModel,
    myInfo: UserInformationViewModel,
    mainNavController : NavHostController
){
    var inputID by remember { mutableStateOf("") }
    var inputPW by remember { mutableStateOf("") }
    val noticeRequirementsText : List<List<String>> = listOf(
        // about id
        listOf(
            "숫자, 영어 대소문자 조합 5자 이상, 10자 이내",
            "아이디는 숫자, 영어 대소문자 조합 5자 이상, 10자 이내여야 합니다."
        ),
        // about password
        listOf(
            "숫자, 영어 대소문자 조합 10자 이상, 20자 이내",
            "비밀번호는 숫자, 영어 대소문자 조합 10자 이상, 20자 이내여야 합니다."
        )
    )
    val noticeRequirementsColor : List<Color> = listOf(Color.White, Color.Red)
    var isWrongInputID by rememberSaveable { mutableStateOf(0) }
    var isWrongInputPW by rememberSaveable { mutableStateOf(0) }
    val currentContext = LocalContext.current

    Surface (
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF212123)
    ){
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            // 아이디
            Column (horizontalAlignment = Alignment.Start){
                OutlinedTextField(
                    value = inputID,
                    placeholder = { Text(text = "ID 입력", color = Color.White) },
                    textStyle = TextStyle(color = Color.White),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.White,
                        unfocusedContainerColor = Color(0x00000000),
                        focusedContainerColor = Color(0x00000000),
                        unfocusedIndicatorColor = Color(0xFF636365),
                        focusedIndicatorColor = Color.White
                    ),
                    singleLine = true,
                    onValueChange = { input -> if (restrictInputID(input)){ inputID = input } }
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = noticeRequirementsText[0][isWrongInputID],
                    fontSize = 14.sp,
                    color = noticeRequirementsColor[isWrongInputID],
                    modifier = Modifier.width(280.dp).wrapContentHeight()
                )
            }
            Spacer(modifier = Modifier.size(36.dp))

            // 비밀번호
            Column (horizontalAlignment = Alignment.Start){
                OutlinedTextField(
                    value = inputPW,
                    placeholder = { Text(text = "비밀번호 입력", color = Color.White) },
                    textStyle = TextStyle(color = Color.White),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.White,
                        unfocusedContainerColor = Color(0x00000000),
                        focusedContainerColor = Color(0x00000000),
                        unfocusedIndicatorColor = Color(0xFF636365),
                        focusedIndicatorColor = Color.White
                    ),
                    singleLine = true,
                    onValueChange = { input -> if (restrictInputPW(input)){ inputPW = input } }
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = noticeRequirementsText[1][isWrongInputPW],
                    fontSize = 14.sp,
                    color = noticeRequirementsColor[isWrongInputPW],
                    modifier = Modifier.width(280.dp).wrapContentHeight()
                )
            }
            Spacer(modifier = Modifier.size(40.dp))

            // 회원가입 버튼
            Surface (
                modifier = Modifier
                    .width(160.dp)
                    .wrapContentHeight()
                    .clickable {
                        if (CheckRightID(inputID) && CheckRightPW(inputPW)) {
                            isWrongInputID = 0
                            isWrongInputPW = 0
                            loginViewModel.Register(tag, inputID, inputPW) { getUserInformation, isSuccessful ->

                                // register success
                                if (isSuccessful) {
                                    myInfo.userInfo.value = getUserInformation
                                    mainNavController.navigate(HomeNavigationGraph.HOME.name) {
                                        popUpTo(MainNavigationGraph.LOGIN.name) {
                                            inclusive = true
                                        }
                                    }
                                }
                                // the account is already exist or server error
                                else {
                                    isWrongInputID = 0
                                    isWrongInputPW = 0
                                    Toast.makeText(currentContext, "계정 생성 실패. 다시 시도해주세요.", LENGTH_SHORT).show()
                                }
                            }
                        } else {

                            if (CheckRightID(inputID)) { isWrongInputID = 0 }
                            else { isWrongInputID = 1 }

                            if (CheckRightPW(inputPW)) { isWrongInputPW = 0 }
                            else { isWrongInputPW = 1 }
                        }
                    },
                shape = RoundedCornerShape(12),
                color = Color(0xFFFF6E1D)
            ){
                Text(
                    text = "회원가입",
                    color = Color.White,
                    //fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
        }
    }
}

fun restrictInputID(id : String) : Boolean {
    return id.all(){ char -> char.isDigit() || char.isLetter() } && id.length <= 10
}

fun restrictInputPW(pw : String) : Boolean {
    return pw.all(){ char -> char.isDigit() || char.isLetter() } && pw.length <= 20
}

fun CheckRightID (id : String) : Boolean {
    return id.isNotBlank() && id.all(){ char -> char.isDigit() || char.isLetter() } && id.length >= 5 && id.length <= 10
}

fun CheckRightPW (pw : String) : Boolean {
    return pw.isNotBlank() && pw.all(){ char -> char.isDigit() || char.isLetter() } && pw.length >= 10 && pw.length <= 20
}