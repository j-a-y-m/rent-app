package com.phics23.tenant.data.service.profile

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.phics23.tenant.data.model.tenant.Tenant
import com.phics23.tenant.data.room.TenantDao
import com.phics23.tenant.data.room.TenantDatabase
import com.phics23.tenant.data.room.booking.BookingDatabase
import com.phics23.tenant.util.Result
import kotlinx.coroutines.tasks.await
import java.io.File
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class ProfileService @Inject constructor(val firebaseAuth: FirebaseAuth,val firebaseStorage: FirebaseStorage,val firebaseFirestore: FirebaseFirestore, val tenantDao: TenantDao, val tenantDatabase: TenantDatabase, val bookingDatabase: BookingDatabase) {


    suspend fun getTenantProfile() : Result<Tenant>
    {
        var result : Result<Tenant> = Result.Loading()
        val currentUserUid = firebaseAuth.currentUser!!.uid
        val tenantProfileDocRef = firebaseFirestore.collection("Tenants").document(currentUserUid)
        try {
            val tenantProfileData = tenantProfileDocRef.get().await().data
            val tenant = Tenant(
                              tenantProfileData?.get("email")!!.toString(),
                              tenantProfileData["name"]!!.toString(),
                              tenantProfileData["address"]!!.toString(),
                              tenantProfileData["phoneNumber"]!!.toString(),
                              tenantProfileData["displayPicture"]?.toString())
            tenantDao.insertTenant(tenant)
            result = Result.Success(tenant)
        }catch (e : Exception)
        {
            result = Result.Failure(e.message.toString())
        }
        return result
    }

//    suspend fun editTenantProfile(tenant: Tenant) : Result<String>
//    {
//        var result : Result<Tenant> = Result.Loading()
//        val currentUserUid = firebaseAuth.currentUser!!.uid
//        val tenantProfileDocRef = firebaseFirestore.collection("Tenants").document(currentUserUid)
//        try {
//            val tenantProfileData = tenantProfileDocRef.get().await().data
//            val tenant = Tenant(
//                tenantProfileData?.get("email")!!.toString(),
//                tenantProfileData["name"]!!.toString(),
//                tenantProfileData["address"]!!.toString(),
//                tenantProfileData["phoneNumber"]!!.toString(),
//                null)
//            tenantDao.insertTenant(tenant)
//            result = Result.Success(tenant)
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

//                    val profileUpdateRequest = userProfileChangeRequest {
//                        setPhotoUri(uriResult.data)
//                    }
//                    firebaseAuth.currentUser!!.updateProfile(profileUpdateRequest).await()

                    val displayPictureData = mutableMapOf<String,String?>("displayPicture" to uriResult.data.toString())
                    firebaseFirestore.collection("Tenants").document(firebaseAuth.currentUser.uid).update(displayPictureData as Map<String, Any>).await()

                    val tenant = tenantDao.getTenant()
                    tenant.displayImageUrl=uriResult.data.toString()
                    tenantDao.updateTenant(tenant)
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
        bookingDatabase.clearAllTables()
        tenantDatabase.clearAllTables()
    }

    suspend fun resetPassword(): Result<String> {
        try {
            firebaseAuth.sendPasswordResetEmail(tenantDao.getTenant().email)
            return Result.Success("Password reset email sent")
        }catch (e : Exception)
        {
            return Result.Failure(e.message.toString())
        }

    }
}