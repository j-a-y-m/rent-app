package com.phics23.owner.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phics23.owner.data.model.owner.Owner
import com.phics23.owner.data.repository.ProfileRepository
import com.phics23.owner.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val profileRepository: ProfileRepository)  : ViewModel() {

    private val _ownerProfile: MutableLiveData<Result<Owner>> = MutableLiveData<Result<Owner>>()
    public val ownerProfile : LiveData<Result<Owner>> by this::_ownerProfile

    private val _setProfileImageResult: MutableLiveData<Result<String>> = MutableLiveData<Result<String>>()
    public val setProfileImageResult : LiveData<Result<String>> by this::_setProfileImageResult

    private val _passwordResetRequestResult: MutableLiveData<Result<String>> = MutableLiveData<Result<String>>()
    public val passwordResetRequestResult : LiveData<Result<String>> by this::_passwordResetRequestResult

    fun getOwnerProfile()
    {
        viewModelScope.launch(Dispatchers.IO) {
            _ownerProfile.postValue(profileRepository.getOwnerProfile())
        }
    }

    fun setProfileImage(imageFile : File)
    {
        viewModelScope.launch(Dispatchers.IO) {
            _setProfileImageResult.postValue(profileRepository.setProfileImage(imageFile))
            getOwnerProfile()
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