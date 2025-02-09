package com.androidsrit.chatroom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter

fun navigateTo(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(route)
        launchSingleTop = true

    }

}

@Composable
fun CommonDivider(){
    HorizontalDivider(
        modifier = Modifier
            .alpha(0.3f)
            .padding(top = 8.dp, bottom = 8.dp),
        thickness = 1.dp,
        color = Color.LightGray
    )
}

@Composable
fun commonImage(data: String?,
                modifier: Modifier = Modifier.wrapContentSize(),
               contentScale: ContentScale  = ContentScale.Crop){
    val painter = rememberAsyncImagePainter(data)

    Image(painter = painter, null, modifier = Modifier, contentScale = contentScale)
}
@Composable
fun commonProgressBar() {
    Row(
        modifier = Modifier
            .alpha(0.5f)
            .background(Color.LightGray)
            .clickable(enabled = false){}
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()

    }

}

@Composable
fun checkSignedIn(vm: CRViewModel, navController: NavController){
        var alreadySignedIn = remember{
            mutableStateOf(false)
        }
    var signedIn = vm.SignIn.value

    if(signedIn && !alreadySignedIn.value){
        alreadySignedIn.value = true
        navController.navigate(DestinationScreen.ChatList.route){ popUpTo(0)}

    }
}
@Composable
fun TitleText(txt:String){
    Text(txt, fontWeight = FontWeight.Bold, fontSize = 35.sp,modifier = Modifier.padding(8.dp))

}

