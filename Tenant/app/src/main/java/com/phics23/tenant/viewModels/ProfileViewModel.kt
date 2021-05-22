package com.phics23.tenant.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phics23.tenant.data.model.tenant.Tenant
import com.phics23.tenant.data.repository.ProfileRepository
import com.phics23.tenant.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val profileRepository: ProfileRepository)  : ViewModel() {

    private val _tenantProfile: MutableLiveData<Result<Tenant>> = MutableLiveData<Result<Tenant>>()
    public val tenantProfile : LiveData<Result<Tenant>> by this::_tenantProfile

    private val _setProfileImageResult: MutableLiveData<Result<String>> = MutableLiveData<Result<String>>()
    public val setProfileImageResult : LiveData<Result<String>> by this::_setProfileImageResult

    private val _passwordResetRequestResult: MutableLiveData<Result<String>> = MutableLiveData<Result<String>>()
    public val passwordResetRequestResult : LiveData<Result<String>> by this::_passwordResetRequestResult

    fun getTenantProfile()
    {
        viewModelScope.launch(Dispatchers.IO) {
            _tenantProfile.postValue(profileRepository.getTenantProfile())
        }
    }

    fun setProfileImage(imageFile : File)
    {
        viewModelScope.launch(Dispatchers.IO) {
            _setProfileImageResult.postValue(profileRepository.setProfileImage(imageFile))
            getTenantProfile()
        }
    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO)
        {
            profileRepository.logOut()
        }

    }

    fun resetPassword() {
        viewModelScope.launch(Dispatchers.IO)
        {
            _passwordResetRequestResult.postValue(profileRepository.resetPassword())
        }
    }
}