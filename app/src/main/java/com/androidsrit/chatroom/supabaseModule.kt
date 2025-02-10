package com.androidsrit.chatroom

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Scoped to SingletonComponent
class SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://qqldleykeowlwayzusuz.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFxbGRsZXlrZW93bHdheXp1c3V6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzkyMTY4NzksImV4cCI6MjA1NDc5Mjg3OX0.fhhGKzfuxq6tIL6BRC4d_--f1OVmDN9I0CfU4vETGM0"
        ) {
            install(GoTrue) // Add GoTrue for authentication (if needed)
            install(Storage) // Add Storage for file uploads
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseStorage(supabaseClient: SupabaseClient): Storage {
        return supabaseClient.storage
    }
}
//@Module
//@InstallIn(SingletonComponent::class)
//class AppModule {
//
//    @Provides
//    @Singleton
//    fun provideImageUploader(storage: Storage): ImageUploader {
//        return ImageUploader(storage)
//    }
//
//
//}