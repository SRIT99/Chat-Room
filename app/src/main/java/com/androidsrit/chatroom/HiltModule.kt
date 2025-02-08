package com.androidsrit.chatroom

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.cloudinary.Cloudinary
import com.cloudinary.android.MediaManager
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)

class HiltModule {
    @Provides
   fun provideAuthentication():FirebaseAuth= Firebase.auth

    @Provides
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideStorage():FirebaseStorage = Firebase.storage



//    @Provides
//    @Singleton
//    fun provideStorage(): Cloudinary
//    {
//        return MediaManager.get().cloudinary
//    }
//    @Provides
//    fun provideContext(@ApplicationContext context: Context): Context {
//        return context
//    }


}