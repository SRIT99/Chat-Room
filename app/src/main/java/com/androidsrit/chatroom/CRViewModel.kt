package com.androidsrit.chatroom

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.androidsrit.chatroom.Data.CHATS
import com.androidsrit.chatroom.Data.ChatData
import com.androidsrit.chatroom.Data.ChatUser
import com.androidsrit.chatroom.Data.Events
import com.androidsrit.chatroom.Data.userData
import com.androidsrit.chatroom.Data.user_Node
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.toObjects
import javax.inject.Inject

@HiltViewModel
class CRViewModel @Inject constructor(
    val auth : FirebaseAuth,
    var db : FirebaseFirestore,
    val storage: FirebaseStorage
): ViewModel() {

    var inProgress = mutableStateOf(false)
    var inChatProgress = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Events<String>?>(null)
    var SignIn = mutableStateOf(false)
    val userData = mutableStateOf<userData?>(null)
    val chats = mutableStateOf<List<ChatData>>(listOf())


    init {
        //init block here
        val curentUser = auth.currentUser
        SignIn.value = curentUser != null
        curentUser?.uid?.let {
            getUserData(it)
        }

    }


    fun SignUp(name: String, number: String, email: String, password: String) {
        inProgress.value = true
        if(name.isEmpty() || number.isEmpty() || email.isEmpty() || password.isEmpty()){
            handleException(customMessage = "Please fill all the fields")
            return
        }
        inProgress.value = true
        db.collection(user_Node).whereEqualTo("number", number)
            .get().addOnSuccessListener {
                if(it.isEmpty()){
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            SignIn.value = true
                            createOrUpdateProfile(name, number)
                        } else {
                            handleException(it.exception, customMessage = "SignUp Failed")

                        }
                    }
                } else{
                    handleException(customMessage = "Number Already Exists")
                    inProgress.value = false
                }
            }
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                SignIn.value = true
                createOrUpdateProfile(name, number)
            } else {
                handleException(it.exception, customMessage = "SignUp Failed")

            }
        }

    }
    fun login(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please Fill All Fields")
            return
        } else {
            inProgress.value = true
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful){
                    SignIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let { getUserData(it) }

                }else{
                    handleException(it.exception, customMessage = "Login Failed")

                }
            }
        }
    }
    fun createOrUpdateProfile(name: String? =null, number: String? = null,  imgUrl: String? = null) {
        var uid = auth.currentUser?.uid
        val UserData = userData(
            userId = uid,
            userName = name?:userData.value?.userName,
            number = number?:userData.value?.number,
            imgUrl = imgUrl?: userData.value?.imgUrl
        )
        uid?.let{
            inProgress.value = true
            db.collection(user_Node).document(uid).get().addOnSuccessListener {
                if(it.exists()){
                    // update user Data
                    db.collection(user_Node).document(uid).set(UserData)
                    inProgress.value= false
                }
                else{
                    db.collection(user_Node).document(uid).set(UserData)
                    inProgress.value = false
                    getUserData(uid)
                }
            }
                .addOnFailureListener {
                    handleException(it, "Cannot Retrive User")
                }

        }


    }

     private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(user_Node).document(uid).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error, "Cannot Retrive User")

            }
            if(value!=null){
                var user = value.toObject<userData>()
                userData.value = user
                inProgress.value = false
                populateChats()

            }

        }

    }

    fun uploadProfileImage(uri: Uri) {
           uploadImage(uri){
            createOrUpdateProfile(imgUrl = it.toString())
        }


    }



 fun uploadImage(uri: Uri, onSuccess:(Uri)->Unit){

     inProgress.value = true
      val storageRef = storage.reference
      val uuid = UUID.randomUUID()
      val imageRef = storageRef.child("images/$uuid")
      val uploadTask = imageRef.putFile(uri)
      uploadTask.addOnSuccessListener {
          val result = it.metadata?.reference?.downloadUrl
          result?.addOnSuccessListener(onSuccess)
          inProgress.value = false
      }
          .addOnFailureListener{
              handleException(it, "Image Upload Failed")
          }
  }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("LiveChatApp", "handleException", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMsg else customMessage

        eventMutableState.value = Events(message)
        inProgress.value = false

    }

    fun onAddChat(number: String) {
        if(number.isEmpty() or ! number.isDigitsOnly()) {
            handleException(customMessage = "Number should only contain digits")
        }else{
            db.collection(CHATS).where(Filter.or(
             Filter.and(
                    Filter.equalTo("user1.number",number),
                    Filter.equalTo("user2.number",userData.value?.number)
                ),
                Filter.and(
                    Filter.equalTo("user1.number",userData.value?.number),
                    Filter.equalTo("user2.number",number)
                )
            )).get().addOnSuccessListener {
                if(it.isEmpty){
                    db.collection(user_Node)
                        .whereEqualTo("number",number)
                        .get()
                        .addOnSuccessListener {
                            if(it.isEmpty){
                                handleException(customMessage = "User Not Found")
                        }else{
                            val chatPartners = it.toObjects<userData>()[0]
                                val id = db.collection(CHATS).document().id
                                val chat = ChatData(
                                    chatId = id,
                                    ChatUser( userId = userData.value?.userId,
                                        name = userData.value?.userName,
                                        imgUrl = userData.value?.imgUrl,
                                        number = userData.value?.number),
                                    ChatUser(
                                        userId = chatPartners.userId,
                                        name = chatPartners.userName,
                                        imgUrl = chatPartners.imgUrl,
                                        number = chatPartners.number)
                                )
                                db.collection(CHATS).document(id).set(chat)
                            }
                          }
                }
                    else{
                    handleException(customMessage = "Chat Already Exists")
                }
            }

        }

    }

    fun populateChats(){
        inChatProgress.value = true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId)
            )
        ).addSnapshotListener{
            value, error ->
            if(error!=null){
                handleException(error)

            }
            if (value !=null){
                chats.value = value.documents.mapNotNull {
                    it.toObject<ChatData>()
                }
                inChatProgress.value = false
            }
        }
    }

}
