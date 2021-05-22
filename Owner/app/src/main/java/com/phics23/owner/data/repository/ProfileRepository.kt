package com.phics23.owner.data.repository

import com.phics23.owner.data.model.owner.Owner
import com.phics23.owner.data.room.OwnerDao
import com.phics23.owner.data.service.profile.ProfileService
import com.phics23.owner.util.Result
import java.io.File
import javax.inject.Inject

class ProfileRepository @Inject constructor(val profileService: ProfileService,val ownerDao: OwnerDao) {

    suspend fun getOwnerProfile(): Result<Owner> {
        var result: Result<Owner> = Result.Loading()

        if (ownerDao.ownerCount() != 0) {
            try {
                val owner = ownerDao.getOwner()
                result = Result.Success(owner)
            } catch (e: Exception) {
                result = Result.Failure(e.message.toString())
            }
        } else {
            result = profileService.getOwnerProfile()
        }
        return result
    }

    suspend fun setProfileImage(imageFile: File): Result<String> {
        return profileService.setProfileImage(imageFile)
    }

    suspend fun logOut() {
        profileService.logout()
        ownerDao.deleteOwner(ownerDao.getOwner())
    }

    suspend fun resetPassword(): Result<String> {
       return profileService.resetPassword()
    }


}