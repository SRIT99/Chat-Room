package com.androidsrit.chatroom

import android.content.Context
import android.net.Uri
import javax.inject.Inject

class ProfileManager @Inject constructor(private val imageUploader: ImageUploader) {
    suspend fun uploadProfileImage(context: Context, uri: Uri) {
        imageUploader.uploadImage(context, uri) { imageUrl ->
            createOrUpdateProfile(imgUrl = imageUrl.toString())
        }
    }

    private fun createOrUpdateProfile(imgUrl: String) {
        // Update the user's profile in your database with the new image URL
        // Example: Update Supabase database or Firebase Firestore
        //kal dekhenge- make this all in view model
    }
}