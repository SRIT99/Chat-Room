package com.androidsrit.chatroom.Screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.androidsrit.chatroom.CRViewModel

@Composable
fun StatusScreen(vm: CRViewModel, navController: NavController) {

    BottomNavMenu(BottomNavigationItems.STATUSLIST, navController)
}