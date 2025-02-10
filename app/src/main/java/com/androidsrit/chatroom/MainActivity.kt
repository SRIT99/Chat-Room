package com.androidsrit.chatroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.androidsrit.chatroom.Screens.ChatList
import com.androidsrit.chatroom.Screens.LoginScreen
import com.androidsrit.chatroom.Screens.ProfileScreen
import com.androidsrit.chatroom.Screens.SignUpScreen
import com.androidsrit.chatroom.Screens.SingleChatScreen
import com.androidsrit.chatroom.Screens.StatusScreen
import com.androidsrit.chatroom.ui.theme.ChatRoomTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(var route:String){
    object SignUp : DestinationScreen(route = "SignUp")
    object Login : DestinationScreen(route = "Login")
    object ChatList : DestinationScreen(route = "ChatList")
    object Profile : DestinationScreen(route = "Profile")
    object SingleChat : DestinationScreen(route = "SingleChat/{ChatId}") {
        fun CreateRoute(ChatId: String) = "SingleChat/$ChatId"
    }
    object Status : DestinationScreen(route = "Status")
    object SingleStatus : DestinationScreen(route = "SingleStatus/{userId}"){
        fun CreateRoute(userId: String)= "SingleChat/$userId"
    }
}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            ChatRoomTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CRNavigation()


                }
            }
        }
    }
    @Composable
    fun CRNavigation(){

        val navController = rememberNavController()
        var vm = hiltViewModel<CRViewModel>()
        NavHost(navController, startDestination = DestinationScreen.SignUp.route){
            composable(DestinationScreen.SignUp.route){
                SignUpScreen(navController, vm)
            }
            composable(DestinationScreen.Login.route){
                LoginScreen(vm, navController)
            }
            composable(DestinationScreen.ChatList.route){
                ChatList(vm, navController)
            }
            composable(DestinationScreen.Status.route){
                StatusScreen(vm, navController)
            }
            composable(DestinationScreen.Profile.route){
                ProfileScreen(vm, navController)
            }
            composable(DestinationScreen.SingleChat.route){
                val chatId = it.arguments?.getString("chatId")
                chatId?.let{
                    SingleChatScreen(vm, navController, chatId = chatId)
                }



            }
        }
//        LoginScreen()

    }
}


