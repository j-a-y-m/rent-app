package com.phics23.owner.data.service.userManagement

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.phics23.owner.util.Result
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class FirebaseAuthService@Inject constructor(val firebaseAuth: FirebaseAuth, val firestore: FirebaseFirestore, val messaging: FirebaseMessaging) {

    suspend fun login(email : String, password : String) : Result<String>
    {
        var result : Result<String> = Result.Loading()

        try {
            val signIn = firebaseAuth.signInWithEmailAndPassword(email,password).await()
            firestore.collection("Owners").document(firebaseAuth.currentUser!!.uid).collection("private").document("details").update("fcmToken",messaging.token.await()).await()
            result = Result.Success("login successful")
        }catch (e:Exception)
        {
            result = Result.Failure(e.message?:"Error occured")
        }

        return result
    }

    suspend fun auth() : Result<String>
    {
        var result : Result<String> = Result.Loading()
        if (firebaseAuth.currentUser != null) {
            result = Result.Success("Logged In")
        } else {
            result = Result.Failure("User not logged in")
        }
        return result
    }

    suspend fun forgotPassword(email: String): Result<String> {

        var result : Result<String> = Result.Loading()

        try {
            val forgotPassword = firebaseAuth.sendPasswordResetEmail(email).await()
            result = Result.Success("reset link has been sent to your mail")
        }catch (e:Exception)
        {
            result = Result.Failure(e.message?:"Error occured")
        }


        return result
    }

}