package com.phics23.owner

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OwnerApp : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}