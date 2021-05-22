package com.phics23.owner.data.service.profile

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.phics23.owner.data.model.owner.Owner
import com.phics23.owner.data.room.OwnerDao
import com.phics23.owner.util.Result
import kotlinx.coroutines.tasks.await
import java.io.File
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class ProfileService @Inject constructor(val firebaseAuth: FirebaseAuth,val firebaseStorage: FirebaseStorage,val firebaseFirestore: FirebaseFirestore, val ownerDao: OwnerDao) {


    suspend fun getOwnerProfile() : Result<Owner>
    {
        var result : Result<Owner> = Result.Loading()
        val currentUserUid = firebaseAuth.currentUser!!.uid
        val ownerProfileDocRef = firebaseFirestore.collection("Owners").document(currentUserUid)
        try {
            val ownerProfileData = ownerProfileDocRef.get().await().data
            val owner = Owner(
                              ownerProfileData?.get("email")!!.toString(),
                              ownerProfileData["name"]!!.toString(),
                              ownerProfileData["address"]!!.toString(),
                              ownerProfileData["phoneNumber"]!!.toString(),
                              ownerProfileData["displayPicture"]?.toString(),)
            ownerDao.insertOwner(owner)
            result = Result.Success(owner)
        }catch (e : Exception)
        {
            result = Result.Failure(e.message.toString())
        }
        return result
    }

//    suspend fun editOwnerProfile(owner: Owner) : Result<String>
//    {
//        var result : Result<Owner> = Result.Loading()
//        val currentUserUid = firebaseAuth.currentUser!!.uid
//        val ownerProfileDocRef = firebaseFirestore.collection("Owners").document(currentUserUid)
//        try {
//            val ownerProfileData = ownerProfileDocRef.get().await().data
//            val owner = Owner(
//                ownerProfileData?.get("email")!!.toString(),
//                ownerProfileData["name"]!!.toString(),
//                ownerProfileData["address"]!!.toString(),
//                ownerProfileData["phoneNumber"]!!.toString(),
//                null)
//            ownerDao.insertOwner(owner)
//            result = Result.Success(owner)
//        }catch (e : Exception)
//        {
//            result = Result.Failure("Failed to retrive profile data")
//        }
//        return result
//    }



    suspend fun getProfileImageUrl() : Result<String?>
    {
        var result : Result<String?> = Result.Loading()
        try {
          result = Result.Success(firebaseAuth.currentUser!!.photoUrl?.toString())
        }catch (e : Exception)
        {
            result = Result.Failure(e.message.toString())
        }
        return result
    }

        suspend fun setProfileImage(imageFile : File) : Result<String>
    {
        var result : Result<String> = Result.Loading()
        val uriResult = uploadProfileImage(imageFile)
        when(uriResult) {
            is Result.Success ->{
                try {
                    val owner = ownerDao.getOwner()
//                    val profileUpdateRequest = userProfileChangeRequest {
//                        setPhotoUri(uriResult.data)
//                    }
//                    firebaseAuth.currentUser!!.updateProfile(profileUpdateRequest).await()
                    val displayPictureData = mutableMapOf<String,String?>("displayPicture" to uriResult.data.toString())
                    firebaseFirestore.collection("Owners").document(firebaseAuth.currentUser.uid).update(displayPictureData as Map<String, Any>).await()

                    owner.displayImageUrl=uriResult.data.toString()
                    ownerDao.updateOwner(owner)
                    result = Result.Success("Profile picture updated")
                }catch (e : Exception)
                {
                    result = Result.Failure(e.message.toString())
                }
            }
            is Result.Failure ->
            {
                result = Result.Failure(uriResult.message.toString())
            }
        }

        return result
    }

    private suspend fun uploadProfileImage(imageFile: File) : Result<Uri>
    {
        var result : Result<Uri> = Result.Loading()
        val folderRef = firebaseStorage.reference.child("Profiles")
        try {
                var imgRef = folderRef.child("${UUID.randomUUID()}")
                imgRef.putFile(Uri.fromFile(imageFile)).await()
                val uri = imgRef.downloadUrl.await()
                result = Result.Success(uri)
            }catch (e : Exception)
            {
                result = Result.Failure(e.message.toString())
                return result
            }

        return result
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun resetPassword(): Result<String> {
        try {
            firebaseAuth.sendPasswordResetEmail(ownerDao.getOwner().email)
            return Result.Success("Password reset email sent")
        }catch (e : Exception)
        {
            return Result.Failure(e.message.toString())
        }

    }


}