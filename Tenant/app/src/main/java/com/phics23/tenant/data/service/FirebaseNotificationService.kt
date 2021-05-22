package com.phics23.tenant.data.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService

class FirebaseNotificationService  : FirebaseMessagingService() {
    val firebase  = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        val uid = auth.uid
        if (uid != null) {
            val data = mutableMapOf<String,String>(
                "uid" to uid,
                "token" to newToken
            )
            firebase.collection("FcmTokens").document(uid).set(data)

        }
        Log.e("fbnotifservice", "onNewToken: "+newToken )

    }
}