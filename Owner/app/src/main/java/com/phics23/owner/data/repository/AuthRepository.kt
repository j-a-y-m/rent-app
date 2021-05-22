package com.phics23.owner.data.repository

import com.phics23.owner.data.service.userManagement.FirebaseAuthService
import com.phics23.owner.util.Result
import javax.inject.Inject


class AuthRepository  @Inject constructor(private val firebaseAuthService: FirebaseAuthService) {


    suspend fun login(email : String, password : String) : Result<String>
    {

       return firebaseAuthService.login(email,password)

    }

    suspend fun forgotPassword(email: String): Result<String> {
            return firebaseAuthService.forgotPassword(email)
    }

    suspend fun auth() : Result<String>
    {
        return firebaseAuthService.auth()
    }
}