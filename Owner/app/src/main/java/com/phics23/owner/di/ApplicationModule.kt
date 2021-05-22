package com.phics23.owner.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.functions.FirebaseFunctions
import com.phics23.owner.data.room.OwnerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import androidx.room.Room.databaseBuilder
import com.google.firebase.functions.ktx.functions
import com.google.firebase.messaging.FirebaseMessaging
import com.phics23.owner.data.room.OwnerDao
import com.phics23.tenant.data.room.booking.BookingDao
import com.phics23.tenant.data.room.booking.BookingDatabase
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Singleton
    @Provides
    fun provideFirebaseAuth() : FirebaseAuth
    {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirestore() : FirebaseFirestore
    {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage() : FirebaseStorage
    {
        return FirebaseStorage.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseMessaging() : FirebaseMessaging
    {
        return  FirebaseMessaging.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseFunctions() : FirebaseFunctions
    {
        return  Firebase.functions
    }

    @Singleton
    @Provides
    fun provideOwnerDao(@ApplicationContext appContext : Context) : OwnerDao
    {
       return  databaseBuilder(appContext,OwnerDatabase::class.java,"Owner-DB").fallbackToDestructiveMigration().build().ownerDao()
    }

    @Singleton
    @Provides
    fun provideBookingDao(@ApplicationContext appContext : Context) : BookingDao
    {
        return  databaseBuilder(appContext, BookingDatabase::class.java,"booking-DB").fallbackToDestructiveMigration().build().bookingDao()
    }

}