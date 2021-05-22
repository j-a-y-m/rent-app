package com.phics23.tenant.data.repository

import com.phics23.tenant.data.model.tenant.Tenant
import com.phics23.tenant.data.room.TenantDao
import com.phics23.tenant.data.service.profile.ProfileService
import com.phics23.tenant.util.Result
import java.io.File
import javax.inject.Inject

class ProfileRepository @Inject constructor(val profileService: ProfileService,val tenantDao: TenantDao) {

    suspend fun getTenantProfile(): Result<Tenant> {
        var result: Result<Tenant> = Result.Loading()

        if (tenantDao.tenantCount() != 0) {
            try {
                val tenant = tenantDao.getTenant()
                result = Result.Success(tenant)
            } catch (e: Exception) {
                result = Result.Failure(e.message.toString())
            }
        } else {
            result = profileService.getTenantProfile()
        }
        return result
    }

    suspend fun setProfileImage(imageFile: File): Result<String> {
        return profileService.setProfileImage(imageFile)
    }

    suspend fun logOut() {
        profileService.logout()
        //tenantDao.deleteTenant(tenantDao.getTenant())
    }

    suspend fun resetPassword(): Result<String> {
        return profileService.resetPassword()
    }


}