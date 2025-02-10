package com.androidsrit.chatroom.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.androidsrit.chatroom.CRViewModel
import com.androidsrit.chatroom.CommonDivider

@Composable
fun SingleChatScreen(vm: CRViewModel, navController: NavController, ChatId: String){

    Text("Single Chat", modifier = Modifier.background(Color.Red))
    ReplyBox(reply = " ", onReplyChange = {}, onSendReply = {})
}
@Composable
fun ReplyBox(reply: String, onReplyChange: (String) -> Unit, onSendReply: () -> Unit){
    Column(modifier = Modifier.fillMaxWidth()){
        CommonDivider()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            TextField(value = reply, onValueChange = onReplyChange, maxLines = 3)

        }

    }

}
