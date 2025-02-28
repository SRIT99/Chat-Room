package com.androidsrit.chatroom.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.androidsrit.chatroom.CRViewModel
import com.androidsrit.chatroom.DestinationScreen
import com.androidsrit.chatroom.TitleText
import com.androidsrit.chatroom.commonProgressBar
import com.androidsrit.chatroom.commonRow
import com.androidsrit.chatroom.navigateTo


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatList(vm: CRViewModel, navController: NavController) {
    val inProgress = vm.inChatProgress.value

    if (inProgress) {
        commonProgressBar()
    } else {
        val chats = vm.chats.value
        val userData = vm.userData.value
        val showDialog = remember {
            mutableStateOf(false)

        }
        val onFabClick: () -> Unit = { showDialog.value = true }
        val onDismiss: () -> Unit = { showDialog.value = false }
        val onAddChat: (String) -> Unit = {
            vm.onAddChat(it)
            showDialog.value = false
        }
        Scaffold(
            floatingActionButton = {
                FAB(
                    showDialog = showDialog.value,
                    onFabClick = onFabClick,
                    onDismiss = onDismiss,
                    onAddChat = onAddChat
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    TitleText("Chats")
                    if (chats.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            Text("No chats Availaible")
                        }
                    } else {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(chats) { chat ->
                                val ChatUser = if (chat.user1.userId == userData?.userId)
                                    chat.user2
                                else chat.user1
                                commonRow(
                                    imgUri = ChatUser.imgUrl,
                                    name = ChatUser.name
                                ) {
                                    chat.chatId?.let {
                                        navigateTo(
                                            navController,
                                            DestinationScreen.SingleChat.CreateRoute(it)
                                        )
                                    }

                                }
                            }
                        }
                    }
                    BottomNavMenu(BottomNavigationItems.CHATLIST, navController)
                }


            }
        )
        //  FAB(showDialog = true, onFabClick = {null}, onDismiss = {null}, onAddChat = {null})

    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FAB(
    showDialog: Boolean,
    onFabClick: () -> Unit,
    onDismiss: () -> Unit,
    onAddChat: (String) -> Unit
) {
    val addChatNumber = remember {
        mutableStateOf(value = "")
    }
    if (showDialog) {
        AlertDialog(onDismissRequest = {
            onDismiss.invoke()
            addChatNumber.value = ""
        },
            confirmButton = {
                Button(onClick = {
                    onAddChat(addChatNumber.value)
                }) {
                    Text("Add Chat")
                }
            },
            title = {
                Text(text = "Add Chat Member")
            },
            text = {
                OutlinedTextField(
                    value = addChatNumber.value,
                    onValueChange = { addChatNumber.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        )

    }
    FloatingActionButton(
        onClick = onFabClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Icon(imageVector = Icons.Rounded.Add, null, tint = Color.White)
    }
}