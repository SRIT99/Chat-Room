package com.androidsrit.chatroom

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
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
import javax.inject.Inject

@HiltViewModel
class CRViewModel @Inject constructor(
    val auth : FirebaseAuth,
    var db : FirebaseFirestore,
    val storage: FirebaseStorage
): ViewModel() {

    var inProgress = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Events<String>?>(null)
    var SignIn = mutableStateOf(false)
    val userData = mutableStateOf<userData?>(null)

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
                            createOrUpdateProfile(name, number, email)
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
                createOrUpdateProfile(name, number, email)
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
            imgUrl =imgUrl?: "https://www.bing.com/ck/a?!&&p=ec39c05d55c21cc5cc0cc96075409b758cf22c4e87a5ce32cf178e6695232e60JmltdHM9MTczODcxMzYwMA&ptn=3&ver=2&hsh=4&fclid=23e106e5-8a51-6a48-04e5-13b48b576b5d&u=a1L2ltYWdlcy9zZWFyY2g_cT1pbWFnZSUyMGlmJTIwZG9nJkZPUk09SVFGUkJBJmlkPTA4NDZGMzQwMjg5ODFBNUQyQUQyQjI0MjJFQjk3RjBCNTlBOUY1RjY&ntb=1"
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

     fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(user_Node).document(uid).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error, "Cannot Retrive User")

            }
            if(value!=null){
                var user = value.toObject<userData>()
                userData.value = user
                inProgress.value = false

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


}