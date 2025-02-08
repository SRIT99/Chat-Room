package com.androidsrit.chatroom.Screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.androidsrit.chatroom.CRViewModel
import com.androidsrit.chatroom.CommonDivider
import com.androidsrit.chatroom.Data.userData
import com.androidsrit.chatroom.DestinationScreen
import com.androidsrit.chatroom.R
import com.androidsrit.chatroom.commonImage
import com.androidsrit.chatroom.commonProgressBar
import com.androidsrit.chatroom.navigateTo

@Composable
fun ProfileScreen(vm:CRViewModel, navController: NavController) {

    if(vm.inProgress.value){
        commonProgressBar()
    }else{
        val userdata = vm.userData.value
        var name by rememberSaveable {
            mutableStateOf(userdata?.userName?:"")
        }
        var number by rememberSaveable {
            mutableStateOf(userdata?.number?:"")
        }
        Column {

            ProfileContent(vm = vm,
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(8.dp),
                name = name,
                number = number,
                onNameChange = {name = it},
                onNumberChange = {number = it},
                onBack = {
                        navController.popBackStack()
                },
                onSave = {

                        vm.createOrUpdateProfile(
                            name = name,
                            number = number
                        )
                },
                onLogout = {
                    vm.SignIn.value = false
                    navigateTo(navController, DestinationScreen.Login.route)
                }

            )
            BottomNavMenu(BottomNavigationItems.PROFILE, navController)
        }
    }
}

@Composable
fun ProfileContent(vm: CRViewModel,
                   modifier: Modifier,
                   name: String,
                   number: String,
                   onNameChange: (String) -> Unit,
                   onNumberChange: (String) -> Unit,
                   onBack : ()-> Unit,
                   onSave: () -> Unit,
                   onLogout : ()-> Unit
                   ){
      val  imageUrl = vm.userData.value?.imgUrl
    Column {
        Row (modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text("Back", modifier = Modifier.clickable {
                onBack.invoke()
            })
            Text("Save", modifier = Modifier.clickable {
                onSave.invoke()
            })
        }
        CommonDivider()
        ProfileImage(imgUrl = imageUrl, vm = vm)
        CommonDivider()

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically){
            Text("Name:", modifier = Modifier.width(100.dp))
            TextField(value = name, onValueChange = onNameChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor =  Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )

                )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically){
            Text("Number:", modifier = Modifier.width(100.dp))
            TextField(value = number, onValueChange = onNumberChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor =  Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
         /*       ,
                trailingIcon = Image(painter = painterResource(id = R.drawable.baseline_edit_24),
                    null,
                    modifier = Modifier.clickable {
                    vm.
                } */



            )
        }
        CommonDivider()
        Row(modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Text("Log Out", color = Color.Red, modifier = Modifier.clickable{
                    onLogout.invoke()
            })
        }

    }

}

@Composable
fun ProfileImage(imgUrl : String?, vm:CRViewModel) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent())
    {
        uri ->
        uri?.let{
            vm.uploadProfileImage(uri)

        }
    }
    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)){
        Column(
            modifier = Modifier
                .padding(8.dp).fillMaxWidth().clickable{
                    launcher.launch("image/*")

                }, horizontalAlignment = Alignment.CenterHorizontally
        ){
            Card(shape = CircleShape, modifier = Modifier.padding(8.dp).size(100.dp)) {
                    commonImage(data = imgUrl)
            }
            Text(text = "Change Profile Picture")
        }
        val isLoading = vm.inProgress.value
        if(isLoading){
            commonProgressBar()
        }


    }


}

