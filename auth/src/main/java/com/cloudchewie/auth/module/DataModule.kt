package com.cloudchewie.auth.module

import android.content.Context
import androidx.room.Room
import com.cloudchewie.auth.OtpTokenDatabase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun database(@ApplicationContext context:Context)
            = Room.databaseBuilder(context, OtpTokenDatabase::class.java, "otp-token-db")
        .build()

    @Singleton
    @Provides
    fun gson() = Gson()
}
