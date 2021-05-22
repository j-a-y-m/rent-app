package com.phics23.owner.data.repository

import com.phics23.owner.data.model.owner.NewOwner
import com.phics23.owner.data.service.userManagement.SignupService
import com.phics23.owner.util.Result
import javax.inject.Inject

class SignupRepository @Inject constructor(val signupService: SignupService) {


    suspend fun signUp(
        owner: NewOwner
    ) : Result<String>
    {
        return signupService.signupUser( owner)
    }

}