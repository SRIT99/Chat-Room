package com.androidsrit.chatroom

import android.content.Context
import android.net.Uri
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject

class ImageUploader @Inject constructor(
    private val storage: Storage // Inject Storage via Hilt
) {
    private val _inProgress = MutableStateFlow(false)
    val inProgress: StateFlow<Boolean> get() = _inProgress

    suspend fun uploadImage(context: Context, uri: Uri, onSuccess: (Uri) -> Unit) {
        _inProgress.value = true
        try {
            // Generate a unique file name
            val uuid = UUID.randomUUID()
            val filePath = "profile-pictures/$uuid" // Use your bucket name here

            // Convert Uri to InputStream
            val inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                handleException(Exception("Failed to open input stream"), "Image Upload Failed")
                return
            }

            // Upload the file to Supabase Storage
            storage.from("profile-picture").upload(filePath, inputStream.readBytes())

            // Construct the public URL manually
            val publicUrl = "https://your-supabase-url.supabase.co/storage/v1/object/public/profile-picture/$filePath"
            onSuccess(Uri.parse(publicUrl))
        } catch (e: Exception) {
            handleException(e, "Image Upload Failed")
        } finally {
            _inProgress.value = false
        }
    }

    private fun handleException(exception: Exception, message: String) {
        // Handle the exception (e.g., log or show an error message)
        println("$message: ${exception.message}")
    }
}