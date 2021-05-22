package com.phics23.tenant.data.service.userManagement

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.phics23.tenant.util.Result
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class FirebaseAuthService@Inject constructor(val firebaseAuth: FirebaseAuth, val firebaseFirestore: FirebaseFirestore) {

    suspend fun login(email : String, password : String) : Result<String>
    {
        var result : Result<String> = Result.Loading()

        try {
            val signIn = firebaseAuth.signInWithEmailAndPassword(email,password).await()
            val uid = firebaseAuth.currentUser!!.uid
            if (firebaseFirestore.collection("Tenants").document(uid).get().await().exists())
            {
                result = Result.Success("login successful")
            }else
            {
                firebaseAuth.signOut()
                throw Exception("Provided credentials are not associated with a Tenant Account")
            }


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