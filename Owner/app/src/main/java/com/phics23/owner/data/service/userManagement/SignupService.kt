package com.phics23.owner.data.service.userManagement

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.phics23.owner.data.model.owner.NewOwner
import com.phics23.owner.util.Result
import kotlinx.coroutines.tasks.await
import java.util.concurrent.ExecutionException
import javax.inject.Inject

class SignupService
@Inject
constructor(val firebaseAuth: FirebaseAuth, val firestore: FirebaseFirestore, val messaging: FirebaseMessaging) {



    suspend fun signupUser(newOwner: NewOwner) : Result<String>
    {
            var result : Result<String> = Result.Loading()

        try {
            val signUp =  firebaseAuth.createUserWithEmailAndPassword(newOwner.email, newOwner.password).await()
            result = createNewUser(signUp.user!!, newOwner.email, newOwner.name , newOwner.address , newOwner.phoneNumber )
            Log.e("authservice", "signupUser: "+signUp.user.email)
        }catch (e:Exception) {
            Log.e("authservice", "signupUser: "+e)
            result = Result.Failure(e.message?:"auth Error occured")
        }


        return result
    }

    suspend fun createNewUser(
        user: FirebaseUser,
        email: String,
        name: String,
        address: String,
        phoneNumber: String
    ) : Result<String>
    {
        val owner = hashMapOf(
            "id" to user.uid,
            "name" to name,
            "email" to email,
            "address" to address,
            "phoneNumber" to phoneNumber,
            "UserType" to "OWNER",
            "displayPicture" to null
        )

        val ownerToken = hashMapOf(
                "fcmToken" to messaging.token.await(),
        )

        val data = mutableMapOf<String,String>(
            "uid" to user.uid,
            "token" to messaging.token.await()
        )


        try {
            val createUser = firestore.collection("Owners").document(user.uid).set(owner).await()
            firestore.collection("Owners").document(user.uid).collection("private").document("details").set(ownerToken).await()
            firestore.collection("FcmTokens").document(user.uid).set(data).await()
            val profileUpdateRequest = userProfileChangeRequest {
                setDisplayName(name)
            }
//            firebaseAuth.currentUser!!.getIdToken(true)
            user.updateProfile(profileUpdateRequest)
            return Result.Success("SignUp successful")
        }catch (e:ExecutionException) {
            Log.e("TAG", "createNewUser: executionException "+e )
            user.delete()
            return Result.Failure(e.message?:"DB Error occured")
        }



    }


}