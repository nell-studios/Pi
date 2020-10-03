package nellStudios.tech.pi.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.repositories.EditProfileRepository

class EditProfileViewModel @ViewModelInject constructor(
    private val repo: EditProfileRepository
): ViewModel() {

    var successfullUpdate: LiveData<Boolean> = MutableLiveData()

    fun updateDetails(user: User) {
        successfullUpdate = repo.setUserDetails(user)
    }
}