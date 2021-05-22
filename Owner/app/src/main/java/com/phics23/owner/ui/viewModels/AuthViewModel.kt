package com.phics23.owner.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phics23.owner.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject
import com.phics23.owner.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginResult: MutableLiveData<Result<String>> = MutableLiveData<Result<String>>()
    public val loginResult: LiveData<Result<String>> by this::_loginResult

    private val _ResetRequestResult: MutableLiveData<Result<String>> = MutableLiveData<Result<String>>()
    public var ResetRequestResult: LiveData<Result<String>> = _ResetRequestResult

    private val _AuthResult: MutableLiveData<Result<String>> = MutableLiveData<Result<String>>()
    public var AuthResult: LiveData<Result<String>> = _AuthResult

     fun login(email: String, password: String) {
            _loginResult.postValue(Result.Loading())
         viewModelScope.launch(Dispatchers.IO)
         {
             if (email.isNotEmpty() && password.isNotEmpty())
             {
                 _loginResult.postValue(authRepository.login(email, password))
             }else
                 _loginResult.postValue(Result.Failure("enter your credentials"))
         }



    }

    fun auth()
    {
        viewModelScope.launch(Dispatchers.IO) {
            _AuthResult.postValue(authRepository.auth())
        }
    }

     fun forgotPassword(email: String) {
         _ResetRequestResult.postValue(Result.Loading())
         viewModelScope.launch(Dispatchers.IO)
         {
             if (email.isNotEmpty()) {
                 _ResetRequestResult.postValue(authRepository.forgotPassword(email))
             } else {
                 _ResetRequestResult.postValue(Result.Failure("enter your e-mail"))
             }
         }

    }

}
