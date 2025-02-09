package com.androidsrit.chatroom.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.androidsrit.chatroom.DestinationScreen
import com.androidsrit.chatroom.R
import com.androidsrit.chatroom.navigateTo

enum class BottomNavigationItems(val icon: Int, val Destination: DestinationScreen){
    CHATLIST(R.drawable.chat, DestinationScreen.ChatList),
    STATUSLIST(R.drawable.status, DestinationScreen.Status),
    PROFILE(R.drawable.user, DestinationScreen.Profile),

}
@Composable
fun BottomNavMenu(selectedItem:BottomNavigationItems,
                  navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .background(Color.White)
    ){
        for(item in BottomNavigationItems.entries){
            Image(painter = painterResource(id = item.icon), null,
                modifier = Modifier.size(40.dp)
                    .padding(4.dp)
                    .weight(1f)
                    .clickable{
                        navigateTo(navController = navController, item.Destination.route)
                    },
                colorFilter = if(item == selectedItem)
                ColorFilter.tint(color = Color.Cyan)
                else
                ColorFilter.tint(color = Color.Gray)



                )
        }

    }
}