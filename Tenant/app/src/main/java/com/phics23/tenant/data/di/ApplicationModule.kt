package com.phics23.tenant.data.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.phics23.tenant.data.room.TenantDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.phics23.tenant.data.room.TenantDao
import com.phics23.tenant.data.room.booking.BookingDao
import com.phics23.tenant.data.room.booking.BookingDatabase
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
    fun provideFirebaseFunctions() : FirebaseFunctions
    {
        return  Firebase.functions
    }

    @Singleton
    @Provides
    fun provideTenantDb(@ApplicationContext appContext : Context) : TenantDatabase
    {
        return  databaseBuilder(appContext,TenantDatabase::class.java,"Tenant-DB").fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideTenantDao(tenantDb : TenantDatabase) : TenantDao
    {
       return  tenantDb.tenantDao()
    }

    @Singleton
    @Provides
    fun provideBookingDb(@ApplicationContext appContext : Context) : BookingDatabase
    {
        return  databaseBuilder(appContext,BookingDatabase::class.java,"booking-DB").fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideBookingDao(bookingDb : BookingDatabase) : BookingDao
    {
        return  bookingDb.bookingDao()
    }

    @Singleton
    @Provides
    fun provideFirebaseMessaging() : FirebaseMessaging
    {
        return  FirebaseMessaging.getInstance()
    }


}