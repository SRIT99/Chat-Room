package com.androidsrit.chatroom.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.androidsrit.chatroom.CRViewModel
import com.androidsrit.chatroom.DestinationScreen
import com.androidsrit.chatroom.R
import com.androidsrit.chatroom.checkSignedIn
import com.androidsrit.chatroom.commonProgressBar
import com.androidsrit.chatroom.navigateTo

@Composable
fun LoginScreen(vm: CRViewModel, navController: NavController){
    checkSignedIn(vm = vm, navController = navController)
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            var emailState = remember { mutableStateOf(TextFieldValue()) }
            var pswState = remember { mutableStateOf(TextFieldValue()) }
            val focus = LocalFocusManager.current
            Image(painter = painterResource(id= R.drawable.img), null ,
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)

            )
            Text(text= "Sign In" ,
                fontSize= 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                modifier = Modifier.padding(8.dp))

            OutlinedTextField(value = emailState.value, onValueChange = {
                emailState.value = it
            },
                label = { Text("Email") },
                modifier = Modifier.padding(8.dp))
            OutlinedTextField(value = pswState.value, onValueChange = {
                pswState.value = it
            },
                label = { Text("Password") },
                modifier = Modifier.padding(8.dp))
            Button(onClick = {
                vm.login(emailState.value.text, pswState.value.text)
            } ,
                modifier = Modifier.padding(8.dp)){
                Text(text = "Sign In")

            }
            Text(text = "Don't have an account? Go to SignUp ->",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.SignUp.route)
                    }
            )
        }
    }
    if (vm.inProgress.value){
        commonProgressBar()
    }

}