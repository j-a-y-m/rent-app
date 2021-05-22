package com.phics23.tenant.ui.viewModels

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phics23.tenant.data.model.tenant.NewTenant
import com.phics23.tenant.data.repository.SignupRepository
import com.phics23.tenant.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignupViewModel @Inject constructor(private val signupRepository: SignupRepository) : ViewModel() {

    private val _signupResult: MutableLiveData<Result<String>> = MutableLiveData<Result<String>>()
    public val signupResult: LiveData<Result<String>> by  this::_signupResult



     fun signUp(
        email : String,
        password : String,
        Name : String,
        address: String,
        phoneNumber: String
    ) {

         viewModelScope.launch(Dispatchers.IO)
         {
             _signupResult.postValue(Result.Loading())
             val tenant = NewTenant(email, password, Name , address , phoneNumber)
             if(validate(tenant))
             {
                 _signupResult.postValue( signupRepository.signUp(tenant))
             }
         }
    }

    suspend private fun validate(tenant: NewTenant): Boolean {
        if (tenant.email.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(tenant.email).matches()))
        {
            Log.e("TAG", "validate: $tenant.email", )
            _signupResult.postValue(Result.Failure("invalid email address"))
            return false
        }else
        if (tenant.password.length<8){
            _signupResult.postValue(Result.Failure("password should have atleast 8 characters"))
            return false
        }else
        if (tenant.name.isEmpty() || tenant.address.isEmpty() || tenant.phoneNumber.isEmpty()){
            _signupResult.postValue(Result.Failure("Please fill all the details"))
            return false
        }else
        {
            return true
        }

    }


}