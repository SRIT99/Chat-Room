package com.androidsrit.chatroom.Screens

import android.text.Layout
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.androidsrit.chatroom.CRViewModel
import com.androidsrit.chatroom.R
import com.androidsrit.chatroom.commonProgressBar


@Composable
fun ChatList(vm:CRViewModel, navController: NavController) {
val inProgress = vm.inProgress.value
    if(inProgress){
        commonProgressBar()
    }else{
            FAB(showDialog = false, onFabClick = {}, onDismiss = {}, onAddChat = {"name"})
            BottomNavMenu(BottomNavigationItems.CHATLIST, navController)
        }
    }

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FAB(
    showDialog: Boolean,
    onFabClick:()->Unit,
    onDismiss:()->Unit,
    onAddChat:(String)->Unit
) {
    val addChatMember = remember {
        mutableStateOf(value = "")
    }
    if (showDialog) {
        AlertDialog(onDismissRequest = {
            onDismiss.invoke()
            addChatMember.value = ""
        },
            confirmButton = {
                Button(onClick = {
                    onAddChat(addChatMember.value)
                }) {
                    Text("Add Chat")
                }
            },
            title = {
                Text(text = "Add Chat Member")
            },
            text = {
                OutlinedTextField(
                    value = addChatMember.value,
                    onValueChange = { addChatMember.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        )
        FloatingActionButton(onClick = {
            onFabClick
        },
            containerColor = MaterialTheme.colorScheme.secondary,
            shape = CircleShape,
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            Icon(imageVector = Icons.Rounded.Add, null)
        }
    }
}