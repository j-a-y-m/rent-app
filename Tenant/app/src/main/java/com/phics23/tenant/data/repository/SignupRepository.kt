package com.phics23.tenant.data.repository

import com.phics23.tenant.data.model.tenant.NewTenant
import com.phics23.tenant.data.service.userManagement.SignupService
import com.phics23.tenant.util.Result
import javax.inject.Inject

class SignupRepository @Inject constructor(val signupService: SignupService) {


    suspend fun signUp(
        tenant: NewTenant
    ) : Result<String>
    {
        return signupService.signupUser( tenant)
    }

}