package com.phics23.tenant

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TenantApp : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}